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

    /**
     * Task to be used.
     */
    private ExpandTask task;

    /**
     * Re-target-able archive to be expanded.
     */
    private Retargetable archive;

    /**
     * Re-target-able directory where files will be expanded.
     */
    private Retargetable directory;

    /**
     * Constructor.
     *
     * @param task {@link ExpandTask} to be used
     * @since 1.0
     */
    @Inject
    ExpandBuilderImpl( final ExpandTask task )
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

    /**
     * Archive to be expanded. It can be in any of zip/jar/tar formats (format determined by extension).
     *
     * @param archive to be expanded
     * @return itself, for fluent API usage
     * @since 1.0
     */
    public ExpandBuilderImpl archive( final FileRef archive )
    {
        this.archive.setFileRef( archive );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandBuilderImpl overwriteNewer()
    {
        task().setOverwriteNewer( true );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandBuilderImpl doNotOverwriteNewer()
    {
        task().setOverwriteNewer( false );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandBuilderImpl cutDirectories( final int directoriesToCut )
    {
        task().setDirectoriesToCut( directoriesToCut );
        return this;

    }

    @Override
    public ExpandBuilder include( final String pattern )
    {
        task().addIncludePattern( pattern );
        return this;
    }

    @Override
    public ExpandBuilder exclude( final String pattern )
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
    public DestinationBuilder to()
    {
        return new DestinationBuilder()
        {
            /**
             * {@inheritDoc}
             *
             * @since 1.0
             */
            @Override
            public ExpandBuilderImpl directory( final FileRef directory )
            {
                ExpandBuilderImpl.this.directory.setFileRef( directory );
                return ExpandBuilderImpl.this;
            }

        };
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    ExpandTask task()
    {
        return task;
    }

}
