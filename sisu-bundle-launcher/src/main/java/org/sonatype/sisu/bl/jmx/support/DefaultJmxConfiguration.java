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

package org.sonatype.sisu.bl.jmx.support;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.bl.jmx.JMXConfiguration;

import com.google.common.collect.Maps;

/**
 * Default {@link org.sonatype.sisu.bl.jmx.JMXConfiguration}, which represents the out-of-box Oracle JDK 6+
 * JMX configurable system properties.
 */
@Named
public class DefaultJmxConfiguration
    implements JMXConfiguration
{
  private boolean localConnectorEnabled;

  private boolean authenticationEnabled;

  private boolean sslEnabled;

  private String remoteHost;

  /**
   * null is not set, -1 is random port, zero is valid for a JMXServiceURL so allow it
   */
  private Integer remotePort;

  /**
   * Constructs a JMXConfiguration with default settings equivalent to those described by
   * <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/management/agent.html#gdevf">JMX System
   * Properties</a>
   *
   * Namely this means:
   * <ul>
   * <li>local access is enabled</li>
   * <li>remote access is disabled</li>
   * </ul>
   *
   * Also, should a remote port be set on this instance, then
   * <ul>
   * <li>ssl is enabled</li>
   * <li>authentication is enabled</li>
   * </ul>
   */
  @Inject
  public DefaultJmxConfiguration() {
    // null implies no remote access
    remotePort = null;
    // null implies not setting RMI server hostname
    remoteHost = null;
    // jvm defaults
    localConnectorEnabled = true;
    sslEnabled = true;
    authenticationEnabled = true;
  }

  @Override
  public boolean isLocalConnectorEnabled() {
    return localConnectorEnabled;
  }

  @Override
  public JMXConfiguration setLocalConnectorEnabled(final boolean enabled) {
    this.localConnectorEnabled = enabled;
    return this;
  }

  @Override
  public JMXConfiguration setRemotePort(final Integer remotePort) {
    if (remotePort != null && !RANDOM_JMX_REMOTE_PORT.equals(remotePort)) {
      if (remotePort < 0) {
        throw new IllegalArgumentException("remote port is " + remotePort);
      }
    }
    this.remotePort = remotePort;
    return this;
  }

  @Override
  public Integer getRemotePort() {
    return remotePort;
  }

  @Override
  public boolean isAuthenticationEnabled() {
    return authenticationEnabled;
  }

  @Override
  public JMXConfiguration setAuthenticationEnabled(final boolean enabled) {
    this.authenticationEnabled = enabled;
    return this;
  }

  @Override
  public boolean isSSLEnabled() {
    return sslEnabled;
  }

  @Override
  public JMXConfiguration setSSLEnabled(final boolean enabled) {
    this.sslEnabled = enabled;
    return this;
  }

  @Override
  public JMXConfiguration setRemoteHost(final String host) {
    this.remoteHost = host;
    return this;
  }

  @Override
  public String getRemoteHost() {
    return remoteHost;
  }

  @Override
  public Map<String, String> getSystemProperties() {
    Map<String, String> sysProps = Maps.newHashMap();

    // technically it's possible to disable local access while remote is enabled
    sysProps.put(PROP_COM_SUN_MANAGEMENT_JMXREMOTE, Boolean.toString(isLocalConnectorEnabled()));

    if (getRemotePort() != null) // enable remote access
    {
      // note: possible that port is 0 - 1024, in which case binding may fail at boot if used
      sysProps.put(PROP_COM_SUN_MANAGEMENT_JMXREMOTE_PORT, Integer.toString(getRemotePort()));

      // override ssl only if disabled, since default is enabled
      if (!isSSLEnabled()) {
        sysProps.put(PROP_COM_SUN_MANAGEMENT_JMXREMOTE_SSL, "false");
      }

      // override auth only if disabled, since default is enabled
      if (!isAuthenticationEnabled()) {
        sysProps.put(PROP_COM_SUN_MANAGEMENT_JMXREMOTE_AUTHENTICATE, "false");
      }

      // help a remote JMX client resolve to the correct RMI server host name if set
      if (getRemoteHost() != null) {
        sysProps.put(PROP_JAVA_RMI_SERVER_HOSTNAME, remoteHost);

        // we should prefer IPV4 stack if host is not numeric IP6 address
        if (!getRemoteHost().contains(":")) {
          sysProps.put(PROP_JAVA_NET_PREFER_IPV4_STACK, "true");
        }
      }
    }
    return sysProps;
  }
}
