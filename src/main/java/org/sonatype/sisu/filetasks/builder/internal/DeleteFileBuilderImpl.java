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

import org.sonatype.sisu.filetasks.builder.DeleteFileBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.DeleteFileTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class DeleteFileBuilderImpl
    extends BuilderImpl
    implements DeleteFileBuilder
{

    /**
     * Task to be used.
     */
    private DeleteFileTask task;

    /**
     * Re-target-able file to be deleted.
     */
    private Retargetable target;


    /**
     * Constructor.
     *
     * @param task {@link DeleteFileTask} to be used
     * @since 1.0
     */
    @Inject
    DeleteFileBuilderImpl( final DeleteFileTask task )
    {
        this.task = task;
        target = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFile( file );
            }

        } );
    }

    /**
     * File to be deleted.
     *
     * @param file to be deleted
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteFileBuilderImpl file( FileRef file )
    {
        target.setFileRef( file );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    DeleteFileTask task()
    {
        return task;
    }

}
