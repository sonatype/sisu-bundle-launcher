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

package org.sonatype.sisu.filetasks.support;

import com.google.common.base.Preconditions;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Chmod;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.PrintStream;

/**
 * Wrapper for invoking Ant tasks.
 *
 * @since 1.0
 */
@Named
public class AntHelper2
{

    private final Project ant;

    @Inject
    private Logger logger = LoggerFactory.getLogger( AntHelper2.class );

    @Inject
    public AntHelper2( @Named( "${basedir}" ) final File basedir )
    {
        Preconditions.checkNotNull( basedir );

        ant = new Project();
        initAntLogger( logger, ant );
        //ant.setBaseDir( basedir );
        ant.init();
    }

    protected void initAntLogger( final Logger logger, final Project ant )
    {
        Preconditions.checkNotNull( logger );
        Preconditions.checkNotNull( ant );

        Slf4jAntLoggerAdapter antLogger = new Slf4jAntLoggerAdapter( logger );
        antLogger.setEmacsMode( true );
        antLogger.setOutputPrintStream( System.out );
        antLogger.setErrorPrintStream( System.err );

        if ( logger.isDebugEnabled() )
        {
            antLogger.setMessageOutputLevel( Project.MSG_VERBOSE );
        }
        else
        {
            antLogger.setMessageOutputLevel( Project.MSG_INFO );
        }

        ant.addBuildListener( antLogger );
    }

    public void setProperty( final String name, Object value )
    {
        Preconditions.checkNotNull( name );
        Preconditions.checkNotNull( value );

        String valueAsString = String.valueOf( value );

        Property prop = (Property) createTask( "property" );
        prop.setName( name );
        prop.setValue( valueAsString );
        prop.execute();
    }

    public Task createTask( final String name )
        throws BuildException
    {
        Preconditions.checkNotNull( name );
        return ant.createTask( name );
    }

    public <T extends ProjectComponent> T createTask( final Class<T> type )
    {
        Preconditions.checkNotNull( type );

        T task = null;
        try
        {
            task = type.newInstance();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        task.setProject( ant );
        return task;
    }

    public void mkdir( final File dir )
    {
        Preconditions.checkNotNull( dir );
        Mkdir mkdir = createTask( Mkdir.class );
        mkdir.setDir( dir );
        mkdir.execute();
    }

    public void chmod( final File dir, final String includes, final String perm )
    {
        Chmod chmod = createTask( Chmod.class );
        chmod.setDir( dir );
        chmod.setIncludes( includes );
        chmod.setPerm( perm );
        chmod.execute();
    }

    /**
     * Replace the given token in the given file with the given replacement.
     * <p/>
     * Caution: repeatedly replacing contents of the same files will most
     * likely fail due to file locking on Windows.
     *
     * @param theFile     the file in which to replace tokens
     * @param token       the token to replace
     * @param replacement the replacement string that will replace the token.
     */
    public void replaceInFile( File theFile, String token, String replacement )
    {
        if ( Os.isFamily( Os.FAMILY_WINDOWS ) )
        {
            logger.warn(
                "Caution: repeatedly replacing bits in the same files on Windows will most likely fail due to file "
                    + "locking." );
        }
        Replace replacer = createTask( Replace.class );
        replacer.setFile( theFile );
        replacer.setEncoding( "UTF-8" );
        replacer.setToken( token );
        replacer.setValue( replacement );
        replacer.setFailOnNoReplacements( true ); // 1.8.0
        replacer.execute();
    }

    /**
     * Adapts Ant logging to Slf4j Logging.
     */
    private static class Slf4jAntLoggerAdapter
        extends DefaultLogger
    {

        protected Logger logger;

        public Slf4jAntLoggerAdapter()
        {
            this( null );
        }

        public Slf4jAntLoggerAdapter( final Logger logger )
        {
            if ( logger == null )
            {
                this.logger = LoggerFactory.getLogger( Slf4jAntLoggerAdapter.class );
            }
            else
            {
                this.logger = logger;
            }
        }

        @Override
        protected void printMessage( final String message, final PrintStream stream, final int priority )
        {
            Preconditions.checkNotNull( message );
            Preconditions.checkNotNull( stream );

            switch ( priority )
            {
                case Project.MSG_ERR:
                    logger.error( message );
                    break;

                case Project.MSG_WARN:
                    logger.warn( message );
                    break;

                case Project.MSG_INFO:
                    logger.info( message );
                    break;

                case Project.MSG_VERBOSE:
                case Project.MSG_DEBUG:
                    logger.debug( message );
                    break;
            }
        }
    }
}
