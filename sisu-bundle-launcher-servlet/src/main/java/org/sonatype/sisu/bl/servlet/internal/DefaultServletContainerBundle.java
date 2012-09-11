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
package org.sonatype.sisu.bl.servlet.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.sisu.filetasks.FileTaskRunner.onDirectory;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

import java.io.File;
import java.util.List;
import javax.inject.Provider;

import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.sonatype.sisu.bl.servlet.ServletContainerBundle;
import org.sonatype.sisu.bl.servlet.ServletContainerBundleConfiguration;
import org.sonatype.sisu.bl.servlet.WAR;
import org.sonatype.sisu.bl.support.DefaultWebBundle;
import org.sonatype.sisu.bl.support.RunningBundles;
import org.sonatype.sisu.bl.support.port.PortReservationService;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.support.AntHelper;

/**
 * TODO
 *
 * @since 1.0
 */
public abstract class DefaultServletContainerBundle<SCB extends ServletContainerBundle, SCBC extends ServletContainerBundleConfiguration>
    extends DefaultWebBundle<SCB, SCBC>
    implements ServletContainerBundle<SCB, SCBC>
{

    /**
     * Ant Helper.
     * Cannot be null.
     */
    private final AntHelper ant;

    public DefaultServletContainerBundle( final String name,
                                          final Provider<SCBC> configurationProvider,
                                          final RunningBundles runningBundles,
                                          final FileTaskBuilder fileTaskBuilder,
                                          final PortReservationService portReservationService,
                                          final AntHelper ant )
    {
        super( name, configurationProvider, runningBundles, fileTaskBuilder, portReservationService );
        this.ant = checkNotNull( ant );
    }

    @Override
    protected void configure()
        throws Exception
    {
        super.configure();
        installWARs();
    }

    private void installWARs()
    {
        final SCBC config = getConfiguration();
        final List<WAR> wars = config.getWARs();
        for ( WAR war : wars )
        {
            if ( war.getFile().isDirectory() )
            {
                onDirectory( config.getTargetDirectory() ).apply(
                    getFileTaskBuilder().copy()
                        .directory( file( war.getFile() ) )
                        .to().directory( path( getName() + "/" + getWebAppPath() + "/" + war.getContext() ) )
                );
            }
            else
            {
                onDirectory( config.getTargetDirectory() ).apply(
                    getFileTaskBuilder().expand( file( war.getFile() ) )
                        .to().directory( path( getName() + "/" + getWebAppPath() + "/" + war.getContext() ) )
                );
            }
        }
    }

    @Override
    protected String composeApplicationURL()
    {
        return String.format( "http://localhost:%s/", getPort() );
    }

    protected void executeScript( final String script, final boolean spawn, final String... arguments )
    {
        ExecTask exec = ant.createTask( ExecTask.class );
        exec.setExecutable( script );

        if ( spawn )
        {
            exec.setSpawn( true );
        }
        else
        {
            exec.setFailIfExecutionFails( true );
            exec.setFailonerror( true );
        }

        if ( arguments != null )
        {
            for ( String argument : arguments )
            {
                Commandline.Argument arg = exec.createArg();
                arg.setValue( argument );
            }
        }

        exec.setDir( new File( getConfiguration().getTargetDirectory(), getName() ) );
        exec.setFailIfExecutionFails( true );

        final Environment.Variable javaHome = new Environment.Variable();
        javaHome.setKey( "JAVA_HOME" );
        javaHome.setValue( System.getProperty( "java.home" ) );
        exec.addEnv( javaHome );

        if ( arguments != null && arguments.length > 0 )
        {
            log.debug( "Executing {} with arguments: {}", script, arguments );
        }
        else
        {
            log.debug( "Executing {}", script );
        }

        exec.execute();
    }

    protected abstract String getWebAppPath();

}
