/*
 * Copyright (c) 2008-2012 Sonatype, Inc. All rights reserved.
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

import org.sonatype.sisu.bl.servlet.jetty.JettyBundle;
import org.sonatype.sisu.bl.servlet.jetty.JettyBundleConfiguration;
import org.sonatype.sisu.bl.support.RunningBundles;
import org.sonatype.sisu.bl.support.port.PortReservationService;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.support.AntHelper;

/**
 * {@link JettyBundle} implementation.
 *
 * @since 1.4
 */
@Named
public class DefaultJettyBundle
    extends JettyBasedBundleSupport<JettyBundle, JettyBundleConfiguration>
    implements JettyBundle
{

    @Inject
    public DefaultJettyBundle( final Provider<JettyBundleConfiguration> configurationProvider,
                               final RunningBundles runningBundles,
                               final FileTaskBuilder fileTaskBuilder,
                               final PortReservationService portReservationService,
                               final AntHelper ant )
    {
        super(
            configurationProvider, runningBundles, fileTaskBuilder, portReservationService, ant
        );
    }
}
