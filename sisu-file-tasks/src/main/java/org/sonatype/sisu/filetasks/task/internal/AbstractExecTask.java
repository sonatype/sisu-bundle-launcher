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
package org.sonatype.sisu.filetasks.task.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.sonatype.sisu.filetasks.task.ExecSettings;

/**
 * Base class for exec tasks. Prepares common exec tasks settings.
 *
 * @since 1.3
 */
abstract class AbstractExecTask<AET extends AbstractExecTask>
    extends AbstractAntTask<ExecTask>
    implements ExecSettings
{

    /**
     * Working directory for the executed process.
     */
    private File workDir;

    /**
     * Executable/script to be executed.
     */
    private String executable;

    /**
     * Executable arguments.
     * Never null.
     */
    private final List<String> arguments;

    /**
     * Environment variables.
     * Never null.
     */
    private final Map<String, String> envVars;

    /**
     * If we should set the JAVA_HOME env. variable as the one of current JVM.
     */
    private boolean useSameJavaHome;

    @Inject
    AbstractExecTask()
    {
        useSameJavaHome = false;
        arguments = new ArrayList<String>();
        envVars = new HashMap<String, String>();
    }

    /**
     * Returns a {@link ExecTask} ANT task.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Class<ExecTask> antTaskType()
    {
        return ExecTask.class;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final ExecTask exec )
    {
        exec.setDir( workDir );

        exec.setExecutable( executable );

        if ( arguments.size() > 0 )
        {
            for ( final String entry : arguments )
            {
                final Commandline.Argument arg = exec.createArg();
                arg.setValue( entry );
            }
        }

        if ( envVars != null )
        {
            for ( final Map.Entry<String, String> entry : envVars.entrySet() )
            {
                final Environment.Variable env = new Environment.Variable();
                env.setKey( entry.getKey() );
                env.setValue( entry.getValue() );
                exec.addEnv( env );
            }
        }
        if ( useSameJavaHome )
        {
            final Environment.Variable javaHome = new Environment.Variable();
            javaHome.setKey( "JAVA_HOME" );
            javaHome.setValue( System.getProperty( "java.home" ) );
            exec.addEnv( javaHome );
        }
    }

    @Override
    public AET setWorkingDirectory( final File workDir )
    {
        this.workDir = workDir;
        return (AET) this;
    }

    @Override
    public AET setExecutable( final String executable )
    {
        this.executable = executable;
        return (AET) this;
    }

    @Override
    public AET addArgument( final String argument )
    {
        this.arguments.add( argument );
        return (AET) this;
    }

    @Override
    public AET addArguments( final List<String> arguments )
    {
        this.arguments.addAll( checkNotNull( arguments ) );
        return (AET) this;
    }

    @Override
    public AET addArguments( final String[] arguments )
    {
        return addArguments( Arrays.asList( checkNotNull( arguments ) ) );
    }

    @Override
    public AET setArguments( final List<String> arguments )
    {
        checkNotNull( arguments );
        this.arguments.clear();
        this.arguments.addAll( arguments );
        return (AET) this;
    }

    @Override
    public AET setArguments( final String[] arguments )
    {
        return setArguments( Arrays.asList( checkNotNull( arguments ) ) );
    }

    @Override
    public ExecSettings addEnv( final String name, final String value )
    {
        this.envVars.put( checkNotNull( name ), checkNotNull( value ) );
        return (AET) this;
    }

    @Override
    public ExecSettings addEnv( final Map<String, String> env )
    {
        this.envVars.putAll( checkNotNull( env ) );
        return (AET) this;
    }

    @Override
    public ExecSettings setEnv( final Map<String, String> env )
    {
        checkNotNull( env );
        this.envVars.clear();
        this.envVars.putAll( env );
        return (AET) this;
    }

    @Override
    public AET setUseSameJavaHome( boolean useSameJavaHome )
    {
        this.useSameJavaHome = useSameJavaHome;
        return (AET) this;
    }

}
