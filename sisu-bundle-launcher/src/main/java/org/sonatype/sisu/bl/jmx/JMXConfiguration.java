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

package org.sonatype.sisu.bl.jmx;

import java.util.Map;

/**
 * Configuration of JMX Connector for a bundle, based on what is configurable via common JDK system properties.
 *
 * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/management/agent.html#gdevf">JMX
 * Configuration</a>
 */
public interface JMXConfiguration<J extends JMXConfiguration>
{
  /**
   * RMI server hostname should be configured to match a host name or IP that can be resolved remotely
   */
  String PROP_JAVA_RMI_SERVER_HOSTNAME = "java.rmi.server.hostname";

  /**
   * Using IPv4 stack prevents common networking problems accessing JMX over a network with IPv4 host name.
   */
  String PROP_JAVA_NET_PREFER_IPV4_STACK = "java.net.preferIPv4Stack";

  /**
   * Property that enables or disabled local memory mapped file JMX access
   */
  String PROP_COM_SUN_MANAGEMENT_JMXREMOTE = "com.sun.management.jmxremote";

  /**
   * 'false' means disable authentication
   */
  String PROP_COM_SUN_MANAGEMENT_JMXREMOTE_AUTHENTICATE = "com.sun.management.jmxremote.authenticate";

  /**
   * Port to expose the JMX agent on - setting this enables remote JMX access on a given port
   */
  String PROP_COM_SUN_MANAGEMENT_JMXREMOTE_PORT = "com.sun.management.jmxremote.port";

  /**
   * 'false' means disable SSL.
   */
  String PROP_COM_SUN_MANAGEMENT_JMXREMOTE_SSL = "com.sun.management.jmxremote.ssl";

  /**
   * Constant that represents a configuration requesting to use a random JMX remote port.
   */
  Integer RANDOM_JMX_REMOTE_PORT = -1;

  /**
   * Flag indicating if the local JMX connector should be enabled?
   * <p/>
   * Local connector is only suitable for connecting by the same user id/file system as the user that started the
   * agent.
   *
   * @return true if local connector should be enabled
   */
  boolean isLocalConnectorEnabled();

  /**
   * Set if a local JVM connector should be enabled.
   *
   * @param enabled true to enable the connector, false to disable it
   */
  J setLocalConnectorEnabled(boolean enabled);

  /**
   * Get the configured remote JMX port number.
   *
   * @return the port that the JMX Agent should listen on or null if no remote access.
   */
  Integer getRemotePort();

  /**
   * Sets the remote port a JMX agent should listen on.
   * <p/>
   * <p>Setting this to a number zero or greater implies remote JMX  access should be enabled.</p>
   *
   * @param remotePort a port number or {@link J#RANDOM_JMX_REMOTE_PORT} if requesting a random free port be used
   *                   or null to not configure remote access
   * @return self, for fluent API
   */
  J setRemotePort(Integer remotePort);

  /**
   * If false then JMX does should not use passwords or access files to authenticate remotely: all users are
   * allowed all access.
   *
   * @return false if authentication is not required to access JMX remotely
   */
  boolean isAuthenticationEnabled();

  /**
   * Set if a remote JVM connector should use authentication methods.
   * <p/>
   * <p>Relevant only if a remote port is also set.</p>
   *
   * @param enabled true to enable connector authentication, false to disable it
   */
  J setAuthenticationEnabled(boolean enabled);

  /**
   * If a remote port is set, then indicate if SSL can be used to access it.
   *
   * @return false if SSL must be used to access JMX remotely
   */
  boolean isSSLEnabled();

  /**
   * Set if a remote JMX client should use SSL protocol remotely.
   *
   * @param enabled true to enable SSL protocol for remote access, false to disable it
   */
  J setSSLEnabled(boolean enabled);

  /**
   * Get the remote JMX host name to use when accessing JMX remotely.
   *
   * @return the configured remote host name.
   */
  String getRemoteHost();

  /**
   * Set the host name to be used when accessing JMX remotely.
   *
   * @param host the host name to use to connect to JMX remotely
   */
  J setRemoteHost(String host);

  /**
   * Get a Map of system properties that can be passed to JVM that enables this configuration.
   *
   * @return a Map of system properties representing the current configuration.
   * @see <a href="http://docs.oracle.com/javase/1.5.0/docs/guide/management/agent.html#mmprops_table">JMX System
   * Properties</a>
   */
  Map<String, String> getSystemProperties();

}
