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

    /**
     * Task to be used.
     */
    private DeleteDirectoryTask task;

    /**
     * Re-target-able directory to be deleted.
     */
    private Retargetable target;

    /**
     * Constructor.
     *
     * @param task {@link DeleteDirectoryTask} to be used
     * @since 1.0
     */
    @Inject
    DeleteDirectoryBuilderImpl( final DeleteDirectoryTask task )
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
     * Directory to be deleted.
     *
     * @param directory to be deleted
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryBuilderImpl directory( FileRef directory )
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
    public DeleteDirectoryBuilderImpl include( final String pattern )
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
    public DeleteDirectoryBuilderImpl exclude( final String pattern )
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
    public DeleteDirectoryBuilderImpl includeEmptyDirectories()
    {
        task().setIncludeEmptyDirectories( true );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryBuilderImpl excludeEmptyDirectories()
    {
        task().setIncludeEmptyDirectories( false );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryBuilder failIfNotPresent()
    {
        task().setFailIfNotPresent( true );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryBuilder doNotFailIfNotPresent()
    {
        task().setFailIfNotPresent( false );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    DeleteDirectoryTask task()
    {
        return task;
    }

}
