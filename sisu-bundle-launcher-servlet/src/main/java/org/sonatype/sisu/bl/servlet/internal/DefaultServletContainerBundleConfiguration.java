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
package org.sonatype.sisu.bl.servlet.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Provider;

import org.sonatype.sisu.bl.jmx.JMXConfiguration;
import org.sonatype.sisu.bl.servlet.ServletContainerBundleConfiguration;
import org.sonatype.sisu.bl.servlet.WAR;
import org.sonatype.sisu.bl.support.DefaultWebBundleConfiguration;

/**
 * TODO
 *
 * @since 1.0
 */
public class DefaultServletContainerBundleConfiguration<SCBC extends ServletContainerBundleConfiguration>
    extends DefaultWebBundleConfiguration<SCBC>
    implements ServletContainerBundleConfiguration<SCBC>
{

    private List<WAR> wars;

    public DefaultServletContainerBundleConfiguration(final Provider<JMXConfiguration> jmxConfigurationProvider )
    {
        super( jmxConfigurationProvider );
        this.wars = new ArrayList<WAR>();
    }

    @Override
    public List<WAR> getWARs()
    {
        return wars;
    }

    @Override
    public SCBC setWARs( final List<WAR> wars )
    {
        this.wars = new ArrayList<WAR>();
        if ( wars != null )
        {
            this.wars.addAll( wars );
        }
        return (SCBC) this;
    }

    @Override
    public SCBC setWARs( final WAR... wars )
    {
        return setWARs( Arrays.asList( wars ) );
    }

    @Override
    public SCBC addWARs( final WAR... wars )
    {
        this.wars.addAll( Arrays.asList( wars ) );
        return (SCBC) this;
    }

}
