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

import org.sonatype.sisu.filetasks.builder.DeleteDirectoryBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.DeleteDirectoryTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class DeleteDirectoryBuilderImpl
    extends BuilderImpl
    implements DeleteDirectoryBuilder
{

    private Retargetable target;

    private DeleteDirectoryTask task;

    @Inject
    DeleteDirectoryBuilderImpl( DeleteDirectoryTask task )
    {
        this.task = task;
        target = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setDirectory( file );
            }

        } );
    }

    DeleteDirectoryBuilderImpl directory( FileRef directory )
    {
        target.setFileRef( directory );
        return this;
    }

    @Override
    public DeleteDirectoryBuilderImpl include( final String pattern )
    {
        task().addIncludePattern( pattern );
        return this;
    }

    @Override
    public DeleteDirectoryBuilderImpl exclude( final String pattern )
    {
        task().addExcludePattern( pattern );
        return this;
    }

    @Override
    public DeleteDirectoryBuilderImpl includeEmptyDirectories()
    {
        task().setIncludeEmptyDirectories( true );
        return this;
    }

    @Override
    public DeleteDirectoryBuilderImpl excludeEmptyDirectories()
    {
        task().setIncludeEmptyDirectories( false );
        return this;
    }

    DeleteDirectoryTask task()
    {
        return task;
    }

}
