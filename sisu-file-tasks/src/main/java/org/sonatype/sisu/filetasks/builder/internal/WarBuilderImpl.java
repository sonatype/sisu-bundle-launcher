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
import org.sonatype.sisu.filetasks.task.ExpandTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import org.sonatype.sisu.filetasks.builder.WarBuilder;
import org.sonatype.sisu.filetasks.task.WarTask;

/**
 * Default {@link WarBuilder} implementation
 *
 * @since 1.0
 */
@Named
public class WarBuilderImpl
    extends BuilderImpl
    implements WarBuilder
{

    /**
     * Task to be used.
     */
    private WarTask task;

    /**
     * Re-target-able archive to be expanded.
     */
    private Retargetable archive;

    /**
     * Constructor.
     *
     * @param task {@link ExpandTask} to be used
     * @since 1.0
     */
    @Inject
    WarBuilderImpl( final WarTask task )
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

    }

    /**
     * Archive to be updated. It can be in any of zip/jar/tar formats (format determined by extension).
     *
     * @param archive to be updated
     * @return itself, for fluent API usage
     * @since 1.0
     */
    public WarBuilderImpl archive( final FileRef archive )
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
    WarTask task()
    {
        return task;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public WarBuilder addLibs(File libDir, String libIncludes) {
        task().addLibs(libDir, libIncludes);
        return this;
    }


}
