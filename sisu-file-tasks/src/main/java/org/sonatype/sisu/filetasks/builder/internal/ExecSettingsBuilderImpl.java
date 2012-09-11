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
package org.sonatype.sisu.filetasks.builder.internal;

import static org.sonatype.sisu.filetasks.builder.FileRef.path;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.sonatype.sisu.filetasks.builder.ExecSettingsBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.ExecSettings;

/**
 * {@link ExecSettingsBuilder} implementation.
 *
 * @since 1.3
 */
abstract class ExecSettingsBuilderImpl<ESB extends ExecSettingsBuilder, ES extends ExecSettings>
    extends BuilderImpl
    implements ExecSettingsBuilder<ESB>
{

    /**
     * Task to be used.
     */
    private ES task;

    /**
     * Re-target-able working directory.
     */
    private Retargetable workDir;

    /**
     * Re-target-able executable.
     */
    private Retargetable executable;

    ExecSettingsBuilderImpl( final ES task )
    {
        this.task = task;
        this.workDir = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setWorkingDirectory( file );
            }

        } );
        this.executable = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setExecutable( file.getAbsolutePath() );
            }

        } );
    }

    @Override
    public ESB executable( final String executable )
    {
        task().setExecutable( executable );
        return (ESB) this;
    }

    @Override
    public ESB executable( final FileRef executable )
    {
        this.executable.setFileRef( executable );
        return (ESB) this;
    }

    @Override
    public ESB script( final String executable )
    {
        return executable( executable );
    }

    @Override
    public ESB script( final FileRef executable )
    {
        return executable( executable );
    }

    @Override
    public ESB on( final FileRef directory )
    {
        this.workDir.setFileRef( directory );
        return (ESB) this;
    }

    @Override
    public ESB useArguments( final List<String> arguments )
    {
        task().setArguments( arguments );
        return (ESB) this;
    }

    @Override
    public ESB useArguments( final String[] arguments )
    {
        task().setArguments( arguments );
        return (ESB) this;
    }

    @Override
    public ESB useEnv( final Map<String, String> env )
    {
        task().setEnv( env );
        return (ESB) this;
    }

    @Override
    public ESB useSameJavaHome()
    {
        task().setUseSameJavaHome( true );
        return (ESB) this;
    }

    @Override
    public ESB withArgument( final String argument )
    {
        task().addArgument( argument );
        return (ESB) this;
    }

    @Override
    public ESB withArguments( final List<String> arguments )
    {
        task().addArguments( arguments );
        return (ESB) this;
    }

    @Override
    public ESB withArguments( final String[] arguments )
    {
        task().addArguments( arguments );
        return (ESB) this;
    }

    @Override
    public ESB withEnv( final Map<String, String> env )
    {
        task().addEnv( env );
        return (ESB) this;
    }

    @Override
    public ESB withEnv( final String name, final String value )
    {
        task().addEnv( name, value );
        return (ESB) this;
    }

    ES task()
    {
        return task;
    }

}
