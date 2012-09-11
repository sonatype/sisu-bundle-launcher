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

import static org.sonatype.sisu.filetasks.FileTaskRunner.onDirectory;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Provider;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.InputStreamFacade;
import org.sonatype.sisu.bl.servlet.internal.DefaultServletContainerBundle;
import org.sonatype.sisu.bl.servlet.jetty.JettyBasedBundle;
import org.sonatype.sisu.bl.servlet.jetty.JettyBasedBundleConfiguration;
import org.sonatype.sisu.bl.support.RunningBundles;
import org.sonatype.sisu.bl.support.port.PortReservationService;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.support.AntHelper;

/**
 * {@link JettyBasedBundle} implementation support.
 *
 * @since 1.4
 */
public abstract class JettyBasedBundleSupport<TB extends JettyBasedBundle, TBC extends JettyBasedBundleConfiguration>
    extends DefaultServletContainerBundle<TB, TBC>
    implements JettyBasedBundle<TB, TBC>
{

    static final String JETTY_XML_LOCATION = "META-INF/org/sonatype/sisu/bl/servlet/jetty/jetty.xml";

    private int httpsPort;

    public JettyBasedBundleSupport( final Provider<TBC> configurationProvider,
                                    final RunningBundles runningBundles,
                                    final FileTaskBuilder fileTaskBuilder,
                                    final PortReservationService portReservationService,
                                    final AntHelper ant )
    {
        super(
            "jetty", configurationProvider, runningBundles, fileTaskBuilder, portReservationService, ant
        );
    }

    @Override
    protected void configure()
        throws Exception
    {
        super.configure();

        httpsPort = getPortReservationService().reservePort();

        copyServerXml();
        makeScriptsExecutable();
    }

    @Override
    protected void unconfigure()
    {
        if ( httpsPort != 0 )
        {
            getPortReservationService().cancelPort( httpsPort );
            httpsPort = 0;
        }
    }

    @Override
    protected void startApplication()
    {
        final String extension = Os.isFamily( Os.FAMILY_WINDOWS ) ? ".bat" : ".sh";
        final File startupScript = new File(
            getConfiguration().getTargetDirectory(), getName() + "/bin/jetty" + extension
        );
        executeScript( startupScript.getAbsolutePath(), false, "start" );
    }

    @Override
    protected void stopApplication()
    {
        final String extension = Os.isFamily( Os.FAMILY_WINDOWS ) ? ".bat" : ".sh";
        final File startupScript = new File(
            getConfiguration().getTargetDirectory(), getName() + "/bin/jetty" + extension
        );
        executeScript( startupScript.getAbsolutePath(), false, "stop" );
    }

    private void makeScriptsExecutable()
    {
        final File binDir = new File( getConfiguration().getTargetDirectory(), getName() + "/bin" );
        getFileTaskBuilder().chmod( file( binDir ) )
            .include( "**/*.sh" )
            .permissions( "u+x" )
            .run();
    }

    private void copyServerXml()
    {
        final TBC config = getConfiguration();

        try
        {
            final File serverXml = File.createTempFile( "jetty-", ".xml" );
            FileUtils.copyStreamToFile(
                new InputStreamFacade()
                {
                    @Override
                    public InputStream getInputStream()
                        throws IOException
                    {
                        final InputStream serverXml = getClass().getClassLoader().getResourceAsStream(
                            JETTY_XML_LOCATION
                        );
                        if ( serverXml == null )
                        {
                            throw new IOException(
                                "Could not find " + JETTY_XML_LOCATION + " on classpath"
                            );
                        }
                        return serverXml;
                    }
                },
                serverXml
            );
            onDirectory( config.getTargetDirectory() ).apply(
                getFileTaskBuilder().copy()
                    .file( file( serverXml ) )
                    .to().file( path( getName() + "/etc/jetty.xml" ) )
                    .filterUsing( "port.https", String.valueOf( httpsPort ) )
                    .filterUsing( "port.http", String.valueOf( getPort() ) )
            );
            serverXml.delete();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    protected String getWebAppPath()
    {
        return "webapps";
    }

    @Override
    protected String generateId()
    {
        return "jetty";
    }

}
