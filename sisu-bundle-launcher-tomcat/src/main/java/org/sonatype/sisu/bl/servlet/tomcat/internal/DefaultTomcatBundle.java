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
package org.sonatype.sisu.bl.servlet.tomcat.internal;

import static org.sonatype.sisu.filetasks.FileTaskRunner.onDirectory;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.InputStreamFacade;
import org.sonatype.sisu.bl.servlet.internal.DefaultServletContainerBundle;
import org.sonatype.sisu.bl.servlet.tomcat.TomcatBundle;
import org.sonatype.sisu.bl.servlet.tomcat.TomcatBundleConfiguration;
import org.sonatype.sisu.bl.support.RunningBundles;
import org.sonatype.sisu.bl.support.port.PortReservationService;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.support.AntHelper;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
public class DefaultTomcatBundle<TB extends TomcatBundle, TBC extends TomcatBundleConfiguration>
    extends DefaultServletContainerBundle<TB, TBC>
    implements TomcatBundle<TB, TBC>
{

    static final String SERVER_XML_LOCATION = "META-INF/org/sonatype/sisu/bl/servlet/tomcat/server.xml";

    private int serverPort;

    @Inject
    public DefaultTomcatBundle( final Provider<TBC> configurationProvider,
                                final RunningBundles runningBundles,
                                final FileTaskBuilder fileTaskBuilder,
                                final PortReservationService portReservationService,
                                final AntHelper ant )
    {
        super(
            "apache-tomcat", configurationProvider, runningBundles, fileTaskBuilder, portReservationService, ant
        );
    }

    @Override
    protected void configure()
        throws Exception
    {
        super.configure();

        serverPort = getPortReservationService().reservePort();

        copyServerXml();
        makeScriptsExecutable();
    }

    @Override
    protected void unconfigure()
    {
        if ( serverPort != 0 )
        {
            getPortReservationService().cancelPort( serverPort );
            serverPort = 0;
        }
    }

    @Override
    protected void startApplication()
    {
        final String extension = Os.isFamily( Os.FAMILY_WINDOWS ) ? ".bat" : ".sh";
        final File startupScript = new File(
            getConfiguration().getTargetDirectory(), getName() + "/bin/startup" + extension
        );
        executeScript( startupScript.getAbsolutePath(), false );
    }

    @Override
    protected void stopApplication()
    {
        final String extension = Os.isFamily( Os.FAMILY_WINDOWS ) ? ".bat" : ".sh";
        final File startupScript = new File(
            getConfiguration().getTargetDirectory(), getName() + "/bin/shutdown" + extension
        );
        executeScript( startupScript.getAbsolutePath(), false, String.valueOf( serverPort ) );
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
            final File serverXml = File.createTempFile( "tomcat-server-", ".xml" );
            FileUtils.copyStreamToFile(
                new InputStreamFacade()
                {
                    @Override
                    public InputStream getInputStream()
                        throws IOException
                    {
                        final InputStream serverXml = getClass().getClassLoader().getResourceAsStream(
                            SERVER_XML_LOCATION
                        );
                        if ( serverXml == null )
                        {
                            throw new IOException(
                                "Could not find " + SERVER_XML_LOCATION + " on classpath"
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
                    .to().file( path( getName() + "/conf/server.xml" ) )
                    .filterUsing( "port.server", String.valueOf( serverPort ) )
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

}
