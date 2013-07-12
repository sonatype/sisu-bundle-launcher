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

import java.io.File;
import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.ReplaceInFileBuilder;
import org.sonatype.sisu.filetasks.task.ReplaceInFileTask;

/**
 * {@link ReplaceInFileBuilder} implementation.
 *
 * @since 1.3
 */
@Named
class ReplaceInFileBuilderImpl
    extends ReplaceSettingsBuilderImpl<ReplaceInFileBuilder, ReplaceInFileTask>
    implements ReplaceInFileBuilder
{

    /**
     * Re-target-able file in which replacements should be made.
     * Never null.
     */
    private final Retargetable fileTarget;

    /**
     * Constructor.
     *
     * @param task {@link ReplaceInFileTask} to be used
     */
    @Inject
    ReplaceInFileBuilderImpl( final ReplaceInFileTask task )
    {
        super( task );
        fileTarget = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFile( file );
            }

        } );
    }

    /**
     * File in which replacements should be made.
     *
     * @param file in which replacements should be made. Cannot be null.
     * @return itself, for fluent API usage
     */
    ReplaceInFileBuilderImpl file( final FileRef file )
    {
        this.fileTarget.setFileRef( file );
        return this;
    }

    @Override
    public ReplaceInFileBuilder doNotFailIfFileDoesNotExist()
    {
        task().setFailIfFileDoesNotExist( false );
        return this;
    }

    @Override
    public ReplaceInFileBuilder failIfFileDoesNotExist()
    {
        task().setFailIfFileDoesNotExist( true );
        return this;
    }
}
