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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.sonatype.sisu.bl.DropwizardBundleConfiguration;
import org.sonatype.sisu.bl.jmx.JMXConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link DropwizardBundleConfiguration} implementation.
 *
 * @since 1.8
 */
@Named
public class DefaultDropwizardBundleConfiguration
    extends DefaultWebBundleConfiguration<DropwizardBundleConfiguration>
    implements DropwizardBundleConfiguration
{

  /**
   * Port on which the admin application will be accessible.
   */
  private int adminPort;

  /**
   * Name of application jar.
   */
  private String jarName;

  /**
   * Yaml file used to configure the application.
   */
  private File yaml;

  /**
   * Arguments to be passed to application (used as {@code java -jar <jar name> <arguments> config.yaml}).
   */
  private String[] arguments = new String[0];

  @Inject
  public DefaultDropwizardBundleConfiguration(final Provider<JMXConfiguration> jmxConfigurationProvider) {
    super(jmxConfigurationProvider);
  }

  @Override
  public int getAdminPort() {
    return adminPort;
  }

  @Override
  public DropwizardBundleConfiguration setAdminPort(final int port) {
    return this;
  }

  @Override
  public String jarName() {
    return jarName;
  }

  @Override
  public DropwizardBundleConfiguration setJarName(final String jarName) {
    this.jarName = checkNotNull(jarName, "JAR name cannot be null");
    return this;
  }

  @Override
  public File getYaml() {
    return yaml;
  }

  @Override
  public DropwizardBundleConfiguration setYaml(final File yaml) {
    this.yaml = checkNotNull(yaml, "Yaml file cannot be null");
    return this;
  }

  @Override
  public String[] arguments() {
    return arguments;
  }

  @Override
  public DropwizardBundleConfiguration setArguments(final String... arguments) {
    this.arguments = checkNotNull(arguments, "Arguments file cannot be null");
    return this;
  }

}
