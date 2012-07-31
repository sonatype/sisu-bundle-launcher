/*
 * Copyright (c) 2007-2011 Sonatype, Inc. All rights reserved.
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

package org.sonatype.sisu.jsw.exec.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.support.AntHelper;
import org.sonatype.sisu.jsw.exec.JSWExec;
import org.sonatype.sisu.jsw.monitor.CommandMonitorTalker;
import org.sonatype.sisu.jsw.monitor.CommandMonitorThread;

/**
 * Default {@link JSWExec} implementation.
 *
 * @since 1.0
 */
class JSWExecImpl
    implements JSWExec
{

    private Logger logger = LoggerFactory.getLogger( JSWExecImpl.class );

    private final File controlScript;

    private final String controlScriptCanonicalPath;

    private final AntHelper ant;

    private FileTaskBuilder fileTaskBuilder;

    private String appName;

    private final int monitorPort;

    /**
     * Constructor.
     *
     * @param bundle      bundle directory.
     * @param appName     app name managed by JSW
     * @param monitorPort
     * @param ant         ANT helper  @throws NullPointerException if params are null
     * @throws RuntimeException if the JSW exec script cannot be found for this platform
     * @since 1.0
     */
    public JSWExecImpl( final File bundle, final String appName, final int monitorPort, final AntHelper ant,
                        FileTaskBuilder fileTaskBuilder )
        throws RuntimeException
    {
        checkNotNull( bundle );
        checkArgument( monitorPort > 0 );

        this.appName = checkNotNull( appName );
        this.monitorPort = monitorPort;
        this.ant = checkNotNull( ant );
        this.fileTaskBuilder = checkNotNull( fileTaskBuilder );

        if ( !bundle.isDirectory() )
        {
            throw new RuntimeException( format( "Bundle %s is not a directory:", bundle.getAbsolutePath() ) );
        }

        if ( appName.trim().length() == 0 )
        {
            throw new IllegalArgumentException( "Application name must contain at least one character" );
        }

        boolean windows = Os.isFamily( Os.FAMILY_WINDOWS );
        final String extension = windows ? ".bat" : "";

        final String controlScriptName = this.appName + extension;

        final DirectoryScanner ds = new DirectoryScanner();
        ds.setBasedir( bundle );
        ds.setIncludes( new String[]{ "**/bin/" + controlScriptName } );
        ds.scan();
        String[] scripts = ds.getIncludedFiles();

        if ( scripts.length == 0 )
        {
            throw new RuntimeException(
                format( "Could not find script named %s in %s bundle", controlScriptName, bundle ) );
        }
        if ( scripts.length > 1 )
        {
            throw new RuntimeException(
                format( "Found more then one script named %s in %s bundle", controlScriptName, bundle ) );
        }

        final File binDir = new File( bundle, scripts[0] ).getParentFile();

        this.controlScript = new File( binDir, controlScriptName );

        this.fileTaskBuilder.chmod( file( binDir ) )
            .exclude( "**/*.bat" )
            .exclude( "**/*.dll" )
            .exclude( "**/*.exe" )
            .exclude( "**/*.jar" )
            .exclude( "**/*.jnilib" )
            .exclude( "**/*.so" )
            .exclude( "**/*.txt" )
            .exclude( "**/*.xml" )
            .permissions( "u+x" )
            .run();

        if ( !this.controlScript.isFile() || !this.controlScript.canExecute() )
        {
            throw new RuntimeException(
                format( "JSW script %s is not an executable file", this.controlScript.getAbsolutePath() ) );
        }

        try
        {
            this.controlScriptCanonicalPath = this.controlScript.getCanonicalPath();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( format( "Problem getting canonical path to JSW control script %s",
                                                this.controlScript.getAbsolutePath() ), e );
        }

    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    public JSWExecImpl start()
    {
        CommandMonitorTalker.installStopShutdownHook( monitorPort );

        executeJSWScript( "console" );

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    public JSWExecImpl stop()
    {
        new CommandMonitorTalker( monitorPort ).send( CommandMonitorThread.STOP_COMMAND );

        return this;
    }

    protected File getControlScript()
    {
        return this.controlScript;
    }

    private void executeJSWScript( final String command )
    {
        executeJSWScript( command, true );
    }

    private void executeJSWScript( final String command, final boolean spawn )
    {
        File script = getControlScript();

        ExecTask exec = ant.createTask( ExecTask.class );
        exec.setExecutable( this.controlScriptCanonicalPath );

        if ( spawn )
        {
            exec.setSpawn( true );
        }
        else
        {
            exec.setFailIfExecutionFails( true );
            exec.setFailonerror( true );
        }
        Commandline.Argument arg = exec.createArg();
        arg.setValue( command );
        exec.setDir( script.getParentFile() );
        logger.debug( "Executing {} script cmd {} {}",
                      new Object[]{ appName, this.controlScriptCanonicalPath, command } );
        exec.execute();
    }

}
