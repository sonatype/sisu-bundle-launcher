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

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.PrintStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wrapper for invoking Ant tasks.
 *
 * @since 1.0
 */
@Named
public class AntHelper
{

    private final Project ant;

    @Inject
    private Logger logger = LoggerFactory.getLogger( AntHelper.class );

    @Inject
    AntHelper()
    {
        ant = new Project();
        initAntLogger( logger, ant );
        ant.init();
    }

    public <T extends ProjectComponent> T createTask( final Class<T> type )
    {
        checkNotNull( type );

        try
        {
            T task = type.newInstance();
            task.setProject( ant );
            return task;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private void initAntLogger( final Logger logger, final Project ant )
    {
        checkNotNull( logger );
        checkNotNull( ant );

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

    /**
     * Adapts Ant logging to Slf4j Logging.
     */
    private static class Slf4jAntLoggerAdapter
        extends DefaultLogger
    {

        protected Logger logger;

        private Slf4jAntLoggerAdapter( final Logger logger )
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
            checkNotNull( message );
            checkNotNull( stream );

            switch ( priority )
            {
                case Project.MSG_ERR:
                    logger.error( message );
                    break;

                case Project.MSG_WARN:
                    logger.warn( message );
                    break;

                case Project.MSG_INFO:
                case Project.MSG_VERBOSE:
                case Project.MSG_DEBUG:
                    logger.debug( message );
                    break;
            }
        }
    }
}
