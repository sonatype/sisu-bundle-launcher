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

package org.sonatype.sisu.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.sonatype.sisu.bl.jmx.JMXConfiguration;
import org.sonatype.sisu.filetasks.FileTask;

/**
 * An bundle configuration.
 *
 * @since 1.0
 */
public interface BundleConfiguration<T extends BundleConfiguration>
{

  /**
   * Port value when a port should be randomly generated.
   */
  int RANDOM_PORT = 0;

  /**
   * Returns bundle identity.
   *
   * @return bundle identity
   */
  String getId();

  /**
   * Sets bundle identity.
   *
   * @param id bundle identity
   * @return itself, for usage in fluent api
   */
  T setId(String id);

  /**
   * Returns the bundle assembly (zip or tar).
   *
   * @return bundle assembly file
   */
  File getBundle();

  /**
   * Sets bundle assembly.
   *
   * @param bundle a zip/jar/tar file or a directory.
   * @return itself, for usage in fluent api
   */
  T setBundle(File bundle);

  /**
   * Returns the target directory to be used for exploding the bundle.
   *
   * @return target directory to be used for exploding the bundle.
   */
  File getTargetDirectory();

  /**
   * Sets target directory.
   *
   * @param targetDirectory directory where bundle will be exploded
   * @return itself, for usage in fluent api
   */
  T setTargetDirectory(File targetDirectory);

  /**
   * Returns overlays to be applied over exploded bundle.
   *
   * @return overlays to be applied over exploded bundle, always a non null value (eventually empty)
   */
  List<FileTask> getOverlays();

  /**
   * Sets overlays. Provided overlays will overwrite existing overlays.
   * Overlays are applied in provided order.
   *
   * @param overlays overlays to be applied over exploded bundle
   * @return itself, for usage in fluent api
   */
  T setOverlays(FileTask... overlays);

  /**
   * Sets overlays. Provided overlays will overwrite existing overlays.
   * Overlays are applied in provided order.
   *
   * @param overlays overlays to be applied over exploded bundle. Can be null, case when an empty list will be
   *                 used
   * @return itself, for usage in fluent api
   */
  T setOverlays(List<FileTask> overlays);

  /**
   * Append overlays to existing set of overlays.
   *
   * @param overlays overlays to be applied over exploded bundle
   * @return itself, for usage in fluent api
   */
  T addOverlays(FileTask... overlays);

  /**
   * Returns the number of seconds to wait for application to start (boot).
   *
   * @return the number of seconds to wait for application to start (boot)
   */
  Integer getStartTimeout();

  /**
   * Sets start (boot) timeout.
   *
   * @param timeout the number of seconds to wait for application to start (boot)
   * @return itself, for usage in fluent api
   */
  T setStartTimeout(Integer timeout);

  /**
   * Returns debugging port.
   *
   * @return debugging port if debugging is enabled, zero otherwise
   */
  Integer getDebugPort();

  /**
   * Returns debugging suspend status.
   *
   * @return true if debugging is enabled and it should suspend on start
   */
  Boolean isSuspendOnStart();

  /**
   * Enables debugging.
   *
   * @param debugPort      debugging port
   * @param suspendOnStart if debugging should suspend execution on start
   * @return itself, for usage in fluent api
   */
  T enableDebugging(Integer debugPort, Boolean suspendOnStart);

  /**
   * Returns system properties.
   *
   * @return system properties map. Never null.
   */
  Map<String, String> getSystemProperties();

  /**
   * Sets a system property.
   *
   * @param key   system property key
   * @param value system property value
   * @return itself, for usage in fluent api
   */
  T setSystemProperty(String key, String value);

  /**
   * @return JMXConfiguration for the bundle or null if JMX is not configured
   */
  JMXConfiguration getJmxConfiguration();

  /**
   * Set the hostname that should be used when accessing the bundle.
   *
   * @param hostName the not null host name to configure
   * @return the host name that should be used when accessing the web bundle
   */
  T setHostName(String hostName);

  /**
   * Returns the host name that should be used when accessing the bundle, for example '127.0.0.1' or 'localhost'.
   *
   * @return the configured host name, never null
   */
  String getHostName();

  /**
   * Returns java agents to be used with the bundle.
   *
   * @return overlays java agents to be used with the bundle, always a non null value (eventually empty)
   * @since 1.8
   */
  List<JavaAgent> getJavaAgents();

  /**
   * Sets java agents. Provided java agents will overwrite existing overlays.
   *
   * @param javaAgents java agents to be used with the bundle
   * @return itself, for usage in fluent api
   * @since 1.8
   */
  T setJavaAgents(JavaAgent... javaAgents);

  /**
   * Sets java agents. Provided java agents will overwrite existing overlays
   *
   * @param javaAgents java agents to be used with the bundle. Can be null, case when an empty list will be used
   * @return itself, for usage in fluent api
   * @since 1.8
   */
  T setJavaAgents(List<JavaAgent> javaAgents);

  /**
   * Append java agents to existing set of java agents.
   *
   * @param javaAgents java agents to be used with the bundle
   * @return itself, for usage in fluent api
   * @since 1.8
   */
  T addJavaAgents(JavaAgent... javaAgents);

}
