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

import java.io.File;
import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.SymlinkBuilder;
import org.sonatype.sisu.filetasks.task.SymlinkTask;

/**
 * {@link SymlinkBuilder} implementation.
 *
 * @since 1.0
 */
@Named
public class SymlinkBuilderImpl
    extends BuilderImpl
    implements SymlinkBuilder
{

    /**
     * Task to be used.
     */
    private SymlinkTask task;

    /**
     * Re-target-able file/directory to be linked.
     */
    private Retargetable source;

    /**
     * Re-target-able symlink to be created.
     */
    private Retargetable target;

    /**
     * Constructor.
     *
     * @param task {@link SymlinkTask} to be used
     * @since 1.0
     */
    @Inject
    SymlinkBuilderImpl( final SymlinkTask task )
    {
        this.task = task;
        source = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setSource( file );
            }

        } );
        target = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setTarget( file );
            }

        } );
    }

    /**
     * File/directory to be linked.
     *
     * @param source file/directory to be moved
     * @return itself, for fluent API usage
     * @since 1.0
     */
    public SymlinkBuilderImpl source( final FileRef source )
    {
        this.source.setFileRef( source );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public SymlinkBuilderImpl as( final FileRef as )
    {
        this.target.setFileRef( as );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public SymlinkBuilder makeHardLink()
    {
        this.task.setHardLink( true );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    SymlinkTask task()
    {
        return task;
    }

}
