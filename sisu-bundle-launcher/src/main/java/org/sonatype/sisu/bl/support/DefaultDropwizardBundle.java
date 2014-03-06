/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package org.sonatype.sisu.bl.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.sonatype.sisu.bl.DropwizardBundle;
import org.sonatype.sisu.bl.DropwizardBundleConfiguration;
import org.sonatype.sisu.bl.support.port.PortReservationService;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.goodies.common.Time;

import com.google.common.base.Throwables;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.sisu.bl.BundleConfiguration.RANDOM_PORT;
import static org.sonatype.sisu.filetasks.FileTaskRunner.onDirectory;

/**
 * {@link DropwizardBundle} implementation.
 *
 * @since 1.8
 */
@Named
public class DefaultDropwizardBundle
    extends DefaultWebBundle<DropwizardBundle, DropwizardBundleConfiguration>
    implements DropwizardBundle
{

  /**
   * Port on which admin application is running. Should be 0 (zero) if application is not running.
   */
  private int adminPort;

  /**
   * URL admin application is available at. Should be null if application is not running.
   */
  private URL adminUrl;

  private ExecuteWatchdog watchdog;

  private ExecuteStreamHandler streamHandler;

  @Inject
  public DefaultDropwizardBundle(final Provider<DropwizardBundleConfiguration> configurationProvider,
                                 final RunningBundles runningBundles,
                                 final FileTaskBuilder fileTaskBuilder,
                                 final PortReservationService portReservationService)
  {
    super("analytics", configurationProvider, runningBundles, fileTaskBuilder, portReservationService);
  }

  @Override
  protected String generateId() {
    return "analytics";
  }

  @Override
  protected void configure()
      throws Exception
  {
    adminPort = getConfiguration().getAdminPort();
    if (adminPort == RANDOM_PORT) {
      adminPort = portReservationService.reservePort();
    }
    adminUrl = new URL(String.format("http://localhost:%s/", getAdminPort()));

    super.configure();
  }

  @Override
  protected void unconfigure() {
    if (getConfiguration().getPort() == RANDOM_PORT && adminPort > 0) {
      getPortReservationService().cancelPort(adminPort);
    }
    adminPort = 0;
    adminUrl = null;
  }

  @Override
  protected String composeApplicationURL() {
    return String.format("http://localhost:%s/", getPort());
  }

  @Override
  public int getAdminPort() {
    return adminPort;
  }

  @Override
  public URL getAdminUrl() {
    return adminUrl;
  }

  @Override
  public File getBundleDirectory() {
    return new File(getConfiguration().getTargetDirectory(), getName());
  }

  @Override
  protected void startApplication() {
    FileRef yamlConfigPath = FileRef.path("config.yaml");
    File bundleDirectory = getBundleDirectory();

    onDirectory(bundleDirectory).apply(
        getFileTaskBuilder().copy()
            .file(FileRef.file(checkNotNull(
                getConfiguration().getYaml(), "Bundle Yaml file must be configured via bundle configuration"
            )))
            .to().file(yamlConfigPath)
            .filterUsing("port", String.valueOf(getPort()))
            .filterUsing("adminPort", String.valueOf(getAdminPort()))
    );

    CommandLine cmdLine = new CommandLine(new File(System.getProperty("java.home"), "/bin/java"))
        .addArgument("-jar")
        .addArgument(getConfiguration().jarName())
        .addArguments(getConfiguration().arguments())
        .addArgument("config.yaml");

    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(bundleDirectory);
    executor.setWatchdog(watchdog = new ExecuteWatchdog(Time.minutes(5).toMillis()));

    try {
      executor.setStreamHandler(streamHandler = new PumpStreamHandler(
          new FileOutputStream(new File(bundleDirectory, "output.log")))
      );
      executor.execute(cmdLine, new DefaultExecuteResultHandler());
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  protected void stopApplication() {
    if (watchdog != null) {
      watchdog.destroyProcess();
      watchdog = null;
    }
    if (streamHandler != null) {
      streamHandler.stop();
      streamHandler = null;
    }
  }

}
