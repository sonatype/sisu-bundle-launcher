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

import java.io.File;
import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.ReplaceInFilesFromDirectoryBuilder;
import org.sonatype.sisu.filetasks.task.ReplaceInFilesFromDirectoryTask;

/**
 * {@link ReplaceInFilesFromDirectoryBuilder} implementation.
 *
 * @since 1.3
 */
@Named
class ReplaceInFilesFromDirectoryBuilderImpl
    extends ReplaceSettingsBuilderImpl<ReplaceInFilesFromDirectoryBuilder, ReplaceInFilesFromDirectoryTask>
    implements ReplaceInFilesFromDirectoryBuilder
{

    /**
     * Re-target-able directory containing files in which replacements should be made.
     * Never null.
     */
    private final Retargetable target;

    /**
     * Constructor.
     *
     * @param task {@link ReplaceInFilesFromDirectoryTask} to be used
     */
    @Inject
    ReplaceInFilesFromDirectoryBuilderImpl( final ReplaceInFilesFromDirectoryTask task )
    {
        super( task );
        target = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File directory )
            {
                task().setDirectory( directory );
            }

        } );
    }

    /**
     * Directory containing files in which replacements should be made.
     *
     * @param directory in which replacements should be made. Cannot be null.
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryBuilderImpl directory( final FileRef directory )
    {
        this.target.setFileRef( directory );
        return this;
    }

    @Override
    public ReplaceInFilesFromDirectoryBuilder include( final String pattern )
    {
        task().addIncludePattern( pattern );
        return this;
    }

    @Override
    public ReplaceInFilesFromDirectoryBuilder exclude( final String pattern )
    {
        task().addExcludePattern( pattern );
        return this;
    }

    @Override
    public ReplaceInFilesFromDirectoryBuilder failIfDirectoryDoesNotExist()
    {
        task().setFailIfDirectoryDoesNotExist( true );
        return this;
    }

    @Override
    public ReplaceInFilesFromDirectoryBuilder doNotFailIfDirectoryDoesNotExist()
    {
        task().setFailIfDirectoryDoesNotExist( false );
        return this;
    }

}
