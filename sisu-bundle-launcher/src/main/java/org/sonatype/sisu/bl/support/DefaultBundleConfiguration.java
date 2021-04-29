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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.sonatype.sisu.bl.BundleConfiguration;
import org.sonatype.sisu.bl.JavaAgent;
import org.sonatype.sisu.bl.jmx.JMXConfiguration;
import org.sonatype.sisu.bl.support.resolver.BundleResolver;
import org.sonatype.sisu.bl.support.resolver.TargetDirectoryResolver;
import org.sonatype.sisu.filetasks.FileTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link BundleConfiguration} implementation that eventually (if bounded) makes use of
 * {@link BundleResolver} to resolve the bundle file and {@link TargetDirectoryResolver} to resolve target directory.
 *
 * @since 1.0
 */
@Named
public class DefaultBundleConfiguration<T extends BundleConfiguration>
    implements BundleConfiguration<T>
{

  /**
   * Start(boot) timeout in seconds configuration property key.
   */
  public static final String START_TIMEOUT = "DefaultBundleConfiguration.startTimeout";

  /**
   * Default start timeout.
   */
  public static final int START_TIMEOUT_DEFAULT = 60;

  /**
   * Default start timeout in seconds, while suspended waiting for debugger attachment.
   */
  public static final int START_SUSPENDED_TIMEOUT_DEFAULT = 5 * 60;

  /**
   * Default host name value is 127.0.0.1, which may prove more reliable than 'localhost'
   */
  public static final String HOSTNAME_DEFAULT = "127.0.0.1";

  /**
   * System properties.
   * Should never be null.
   */
  private final Map<String, String> systemProperties;

  /**
   * Bundle identity.
   */
  private String id;

  /**
   * Application bundle zip/jar/tar or directory.
   */
  private File bundle;

  /**
   * Application target directory, where bundle will be exploded.
   */
  private File targetDirectory;

  /**
   * List of overlays to be applied to exploded bundle. Should never be null.
   */
  private List<FileTask> overlays;

  /**
   * Number of seconds to wait for application to boot.
   */
  private Integer startTimeout;

  /**
   * Debugging port if debug is enabled. Zero (0) otherwise;
   * Should never be null.
   */
  private Integer debugPort;

  /**
   * True if debugging is enabled and it should suspend execution on start, false otherwise.
   * Should never be null.
   */
  private Boolean suspendOnStart;

  /**
   * Resolver to be used to resolve application bundle file. Can be null.
   * Lazy used (if not null) when bundle file is requested.
   */
  private BundleResolver bundleResolver;

  /**
   * Resolver to be used to resolve application target directory. Can be null.
   * Lazy used (if not null) when target directory is requested.
   */
  private TargetDirectoryResolver targetDirectoryResolver;

  /**
   * Lazy provider of JMX configuration.
   */
  private Provider<JMXConfiguration> jmxConfigurationProvider;

  /**
   * How JMX should be configured on this bundle.
   */
  private JMXConfiguration jmxConfiguration;

  /**
   * Host name to access web bundle with
   */
  private String hostName;

  /**
   * List of java agents to be used with the bundle.
   */
  private List<JavaAgent> javaAgents;

  /**
   * Lists of java options to be used.
   */
  private List<String> javaOptions;

  @Inject
  public DefaultBundleConfiguration(final Provider<JMXConfiguration> jmxConfigurationProvider) {
    this.jmxConfigurationProvider = checkNotNull(jmxConfigurationProvider);
    setOverlays();
    setJavaOptions();
    setJavaAgents();
    debugPort = 0;
    suspendOnStart = false;
    systemProperties = Maps.newHashMap();
    setHostName(HOSTNAME_DEFAULT);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public T setId(final String id) {
    this.id = id;
    return self();
  }

  /**
   * Resolves the bundle (if not already set) by using {@link org.sonatype.sisu.bl.support.resolver.BundleResolver} (if
   * present).
   * 
   * {@inheritDoc}
   */
  @Override
  public File getBundle() {
    if (bundle == null && bundleResolver != null) {
      bundle = bundleResolver.resolve();
    }
    return bundle;
  }

  @Override
  public T setBundle(File bundle) {
    this.bundle = bundle;
    return self();
  }

  /**
   * Resolves the target directory (if not already set) by using {@link org.sonatype.sisu.bl.support.resolver.TargetDirectoryResolver}
   * (if present).
   * 
   * {@inheritDoc}
   */
  @Override
  public File getTargetDirectory() {
    if (targetDirectory == null && targetDirectoryResolver != null) {
      targetDirectory = new File(targetDirectoryResolver.resolve(), getId());
    }
    return targetDirectory;
  }

  @Override
  public T setTargetDirectory(File targetDirectory) {
    this.targetDirectory = targetDirectory;
    return self();
  }

  @Override
  public List<FileTask> getOverlays() {
    return overlays;
  }

  @Override
  public T setOverlays(List<FileTask> overlays) {
    this.overlays = new ArrayList<FileTask>();
    if (overlays != null) {
      this.overlays.addAll(overlays);
    }
    return self();
  }

  @Override
  public T setOverlays(FileTask... overlays) {
    return setOverlays(Arrays.asList(overlays));
  }

  @Override
  public T addOverlays(FileTask... overlays) {
    this.overlays.addAll(Arrays.asList(overlays));
    return self();
  }

  @Override
  public Integer getStartTimeout() {
    return startTimeout;
  }

  @Override
  public T setStartTimeout(final Integer startTimeout) {
    this.startTimeout = startTimeout;
    return self();
  }

  /**
   * Sets number of seconds to wait for application to boot. If injected will use the timeout bounded to
   * {@link #START_TIMEOUT} with a default of {@link #START_TIMEOUT_DEFAULT} seconds.
   * 
   * @param startTimeout the start timeout
   */
  @Inject
  protected void configureStartTimeout(
      final @Named("${" + START_TIMEOUT + ":-" + START_TIMEOUT_DEFAULT + "}") Integer startTimeout)
  {
    setStartTimeout(startTimeout);
  }

  @Override
  public Integer getDebugPort() {
    return debugPort;
  }

  @Override
  public Boolean isSuspendOnStart() {
    return suspendOnStart;
  }

  @Override
  public T enableDebugging(final Integer debugPort, final Boolean suspendOnStart) {
    this.debugPort = checkNotNull(debugPort);
    this.suspendOnStart = checkNotNull(suspendOnStart);
    // suspending Nexus while debugging could cause bundle startup monitoring to fail if we do not increase time
    if (suspendOnStart && getStartTimeout() == START_TIMEOUT_DEFAULT) {
      setStartTimeout(START_SUSPENDED_TIMEOUT_DEFAULT);
    }
    return self();
  }

  @Override
  public Map<String, String> getSystemProperties() {
    return systemProperties;
  }

  @Override
  public T setSystemProperty(String key, String value) {
    systemProperties.put(key, value);
    return self();
  }

  @Override
  public JMXConfiguration getJmxConfiguration() {
    if (this.jmxConfiguration == null) {
      this.jmxConfiguration = jmxConfigurationProvider.get();
    }
    return this.jmxConfiguration;
  }

  /**
   * Sets an optional bundle resolver.
   *
   * @param bundleResolver optional bundle resolver to be used to resolve application bundle if bundle not set
   */
  @Inject
  protected void setBundleResolver(final @Nullable BundleResolver bundleResolver) {
    this.bundleResolver = bundleResolver;
  }

  /**
   * Sets an optional target directory resolver.
   *
   * @param targetDirectoryResolver optional target directory resolver to be used to resolve directory where
   *                                application bundle is exploded if target directory is not set
   */
  @Inject
  protected void setTargetDirectoryResolver(final @Nullable TargetDirectoryResolver targetDirectoryResolver) {
    this.targetDirectoryResolver = targetDirectoryResolver;
  }

  @SuppressWarnings("unchecked")
  protected T self() {
    return (T) this;
  }

  @Override
  public T setHostName(String hostName) {
    this.hostName = checkNotNull(hostName);
    return self();
  }

  /**
   * @return configured hostname or {@link #HOSTNAME_DEFAULT} if not set
   */
  @Override
  public String getHostName() {
    return this.hostName;
  }

  /**
   * @since 1.8
   */
  @Override
  public List<String> getJavaOptions() {
    return javaOptions;
  }

  /**
   * @since 1.8
   */
  @Override
  public T setJavaOptions(List<String> javaOptions) {
    this.javaOptions = Lists.newArrayList();
    if (javaOptions != null) {
      this.javaOptions.addAll(javaOptions);
    }
    return self();
  }

  /**
   * @since 1.8
   */
  @Override
  public T setJavaOptions(String... javaOptions) {
    return setJavaOptions(Arrays.asList(javaOptions));
  }

  /**
   * @since 1.8
   */
  @Override
  public T addJavaOptions(String... javaOptions) {
    this.javaOptions.addAll(Arrays.asList(javaOptions));
    return self();
  }

  /**
   * @since 1.8
   */
  @Override
  public List<JavaAgent> getJavaAgents() {
    return javaAgents;
  }

  /**
   * @since 1.8
   */
  @Override
  public T setJavaAgents(List<JavaAgent> javaAgents) {
    this.javaAgents = new ArrayList<JavaAgent>();
    if (javaAgents != null) {
      this.javaAgents.addAll(javaAgents);
    }
    return self();
  }

  /**
   * @since 1.8
   */
  @Override
  public T setJavaAgents(JavaAgent... javaAgents) {
    return setJavaAgents(Arrays.asList(javaAgents));
  }

  /**
   * @since 1.8
   */
  @Override
  public T addJavaAgents(JavaAgent... javaAgents) {
    this.javaAgents.addAll(Arrays.asList(javaAgents));
    return self();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append("{id=").append(getId());
    sb.append(", hostName=").append(getHostName());
    sb.append(", bundle=").append(getBundle());
    sb.append(", targetDirectory=").append(getTargetDirectory());
    sb.append('}');
    return sb.toString();
  }

}
