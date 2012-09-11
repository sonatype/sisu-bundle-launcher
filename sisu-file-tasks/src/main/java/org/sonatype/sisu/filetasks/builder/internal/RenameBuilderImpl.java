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

import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.RenameBuilder;
import org.sonatype.sisu.filetasks.task.RenameTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
public class RenameBuilderImpl
    extends BuilderImpl
    implements RenameBuilder
{

    /**
     * Task to be used.
     */
    private RenameTask task;

    /**
     * Re-target-able file/directory to be renamed.
     */
    private Retargetable target;

    /**
     * Constructor.
     *
     * @param task {@link RenameTask} to be used
     * @since 1.0
     */
    @Inject
    RenameBuilderImpl( final RenameTask task )
    {
        this.task = task;
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
     * File/directory to be renamed.
     *
     * @param target file/directory to be renamed
     * @return itself, for fluent API usage
     * @since 1.0
     */
    public RenameBuilderImpl target( final FileRef target )
    {
        this.target.setFileRef( target );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public RenameBuilderImpl to( final String name )
    {
        task().setName( name );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    RenameTask task()
    {
        return task;
    }

}
