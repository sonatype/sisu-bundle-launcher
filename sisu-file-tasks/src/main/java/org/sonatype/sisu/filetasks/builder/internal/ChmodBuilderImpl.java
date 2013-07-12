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
package org.sonatype.sisu.filetasks.builder.internal;

import org.sonatype.sisu.filetasks.builder.ChmodBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.ChmodTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class ChmodBuilderImpl
    extends BuilderImpl
    implements ChmodBuilder
{

    /**
     * Task to be used.
     */
    private ChmodTask task;

    /**
     * Re-target-able directory containing files to change permissions.
     */
    private Retargetable target;

    /**
     * Constructor.
     *
     * @param task {@link ChmodTask} to be used
     * @since 1.0
     */
    @Inject
    ChmodBuilderImpl( final ChmodTask task )
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

    /**
     * Specify the directory containing the files to have permission changed.
     *
     * @param directory directory containing the files to have permissions changed
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ChmodBuilderImpl directory( final FileRef directory )
    {
        target.setFileRef( directory );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodBuilderImpl include( final String pattern )
    {
        task().addIncludePattern( pattern );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodBuilderImpl exclude( final String pattern )
    {
        task().addExcludePattern( pattern );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodBuilder permissions( final String permissions )
    {
        task().setPermissions( permissions );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    ChmodTask task()
    {
        return task;
    }

}
