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
package org.sonatype.sisu.bl.servlet.jetty.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.sonatype.sisu.bl.jmx.JMXConfiguration;
import org.sonatype.sisu.bl.servlet.jetty.JettyBundleConfiguration;

/**
 * {@link JettyBundleConfiguration} implementation.
 *
 * @since 1.4
 */
@Named
public class DefaultJettyBundleConfiguration
    extends JettyBasedBundleConfigurationSupport<JettyBundleConfiguration>
    implements JettyBundleConfiguration
{

    @Inject
    public DefaultJettyBundleConfiguration(
        final Provider<JMXConfiguration> jmxConfigurationProvider )
    {
        super( jmxConfigurationProvider );
    }
}
