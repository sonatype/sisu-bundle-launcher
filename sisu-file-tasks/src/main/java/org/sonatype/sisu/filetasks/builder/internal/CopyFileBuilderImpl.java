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

import org.sonatype.sisu.filetasks.builder.CopyFileBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.CopyFileTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class CopyFileBuilderImpl
    extends CopySettingsBuilderImpl<CopyFileBuilder, CopyFileTask>
    implements CopyFileBuilder
{

    /**
     * Re-target-able file to be copied.
     */
    private Retargetable from;

    /**
     * Re-target-able file to copy file to.
     */
    private Retargetable toFile;

    /**
     * Re-target-able directory to copy file to.
     */
    private Retargetable toDirectory;

    /**
     * Constructor.
     *
     * @param task {@link CopyFileTask} to be used
     * @since 1.0
     */
    @Inject
    CopyFileBuilderImpl( final CopyFileTask task )
    {
        super( task );
        from = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFromFile( file );
            }

        } );
        toFile = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setToFile( file );
            }

        } );
        toDirectory = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setToDirectory( file );
            }

        } );
    }

    /**
     * Source file to be copied.
     *
     * @param file to be copied
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyFileBuilderImpl file( FileRef file )
    {
        from.setFileRef( file );
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
            public CopyFileBuilderImpl directory( final FileRef directory )
            {
                toDirectory.setFileRef( directory );
                return CopyFileBuilderImpl.this;
            }

            /**
             * {@inheritDoc}
             *
             * @since 1.0
             */
            @Override
            public CopyFileBuilderImpl file( final FileRef file )
            {
                toFile.setFileRef( file );
                return CopyFileBuilderImpl.this;
            }
        };

    }

}
