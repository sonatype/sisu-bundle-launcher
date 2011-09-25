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

package org.sonatype.sisu.filetasks.builder.internal;

import org.sonatype.sisu.filetasks.builder.ExpandBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.ExpandTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
public class ExpandBuilderImpl
    extends BuilderImpl
    implements ExpandBuilder
{

    private ExpandTask task;

    private Retargetable archive;

    private Retargetable directory;

    @Inject
    ExpandBuilderImpl( ExpandTask task )
    {
        this.task = task;
        archive = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setArchive( file );
            }

        } );
        directory = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setToDirectory( file );
            }

        } );
    }

    public ExpandBuilderImpl archive( final FileRef archive )
    {
        this.archive.setFileRef( archive );
        return this;
    }

    @Override
    public ExpandBuilderImpl overwriteNewer()
    {
        task().setOverwriteNewer( true );
        return this;
    }

    @Override
    public ExpandBuilderImpl doNotOverwriteNewer()
    {
        task().setOverwriteNewer( false );
        return this;
    }

    @Override
    public ExpandBuilderImpl cutDirectories( final int directoriesToCut )
    {
        task().setDirectoriesToCut( directoriesToCut );
        return this;

    }

    @Override
    public PathBuilder to()
    {
        return new PathBuilder()
        {
            @Override
            public ExpandBuilderImpl directory( final FileRef directory )
            {
                ExpandBuilderImpl.this.directory.setFileRef( directory );
                return ExpandBuilderImpl.this;
            }

        };
    }

    ExpandTask task()
    {
        return task;
    }

}
