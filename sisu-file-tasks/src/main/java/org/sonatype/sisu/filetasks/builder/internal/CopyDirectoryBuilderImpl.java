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

import org.sonatype.sisu.filetasks.builder.CopyDirectoryBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.CopyDirectoryTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class CopyDirectoryBuilderImpl
    extends CopySettingsBuilderImpl<CopyDirectoryBuilder, CopyDirectoryTask>
    implements CopyDirectoryBuilder
{

    /**
     * Re-target-able directory to copy files from.
     */
    private Retargetable from;

    /**
     * Re-target-able directory to copy files to.
     */
    private Retargetable to;

    /**
     * Constructor.
     *
     * @param task {@link CopyDirectoryTask} to be used
     * @since 1.0
     */
    @Inject
    CopyDirectoryBuilderImpl( final CopyDirectoryTask task )
    {
        super( task );
        from = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFromDirectory( file );
            }

        } );
        to = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setToDirectory( file );
            }

        } );

    }

    /**
     * Source directory to be copied.
     *
     * @param directory to be copied
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryBuilderImpl directory( FileRef directory )
    {
        from.setFileRef( directory );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyDirectoryBuilderImpl include( final String pattern )
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
    public CopyDirectoryBuilderImpl exclude( final String pattern )
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
    public CopyDirectoryBuilderImpl includeEmptyDirectories()
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
    public CopyDirectoryBuilderImpl excludeEmptyDirectories()
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
    public DestinationBuilder to()
    {
        return new DestinationBuilder()
        {
            @Override
            public CopyDirectoryBuilderImpl directory( final FileRef directory )
            {
                to.setFileRef( directory );
                return CopyDirectoryBuilderImpl.this;
            }
        };
    }

}
