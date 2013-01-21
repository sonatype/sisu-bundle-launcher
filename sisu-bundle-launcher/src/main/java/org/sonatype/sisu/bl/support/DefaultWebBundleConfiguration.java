/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.sonatype.sisu.bl.WebBundleConfiguration;
import org.sonatype.sisu.bl.jmx.JMXConfiguration;

/**
 * Default {@link WebBundleConfiguration} implementation.
 *
 * @since 1.2
 */
@Named
public class DefaultWebBundleConfiguration<T extends WebBundleConfiguration>
    extends DefaultBundleConfiguration<T>
    implements WebBundleConfiguration<T>
{

    /**
     * Port on which the application will be accessible.
     */
    private int port;

    @Inject
    public DefaultWebBundleConfiguration( final Provider<JMXConfiguration> jmxConfigurationProvider )
    {
        super( jmxConfigurationProvider );
        setPort( RANDOM_PORT );
    }

    @Override
    public int getPort()
    {
        return port;
    }

    @Override
    public T setPort( final int port )
    {
        this.port = port;
        return self();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append( getClass().getSimpleName() );
        sb.append( "{" ).append( super.toString() );
        sb.append( ", port=" ).append( getPort() );
        sb.append( '}' );
        return sb.toString();
    }

}
