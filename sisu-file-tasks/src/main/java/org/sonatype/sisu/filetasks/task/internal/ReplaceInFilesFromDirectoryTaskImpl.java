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
package org.sonatype.sisu.filetasks.task.internal;

import java.io.File;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.tools.ant.taskdefs.Replace;
import org.sonatype.sisu.filetasks.task.ReplaceInFilesFromDirectoryTask;
import com.google.common.collect.Lists;

/**
 * ANT based {@link ReplaceInFilesFromDirectoryTask} implementation.
 *
 * @since 1.3
 */
@Named
class ReplaceInFilesFromDirectoryTaskImpl
    extends AbstractReplaceTask<ReplaceInFilesFromDirectoryTaskImpl>
    implements ReplaceInFilesFromDirectoryTask
{

    /**
     * Directory containing files in which replacements should be made.
     */
    private File directory;

    /**
     * Include patterns to filter files that will be processed.
     * Never null.
     */
    private final List<String> includes;

    /**
     * Exclude patterns to filter files that will be processed.
     * Never null.
     */
    private final List<String> excludes;

    /**
     * True if replace should fail if directory does not exist.
     */
    private boolean failIfDirectoryDoesNotExist;

    @Inject
    ReplaceInFilesFromDirectoryTaskImpl()
    {
        includes = Lists.newArrayList();
        excludes = Lists.newArrayList();
        failIfDirectoryDoesNotExist = true;
    }

    /**
     * Returns false if file does not exist, true otherwise.
     * 
     * {@inheritDoc}
     */
    @Override
    boolean shouldExecute()
    {
        return super.shouldExecute() && ( failIfDirectoryDoesNotExist || directory.exists() );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Replace task )
    {
        super.prepare( task );
        task.setDir( directory );
        if ( includes.size() > 0 )
        {
            task.setIncludes( merge( includes ) );
        }
        if ( excludes.size() > 0 )
        {
            task.setExcludes( merge( excludes ) );
        }
    }

    @Override
    public ReplaceInFilesFromDirectoryTaskImpl setDirectory( final File directory )
    {
        this.directory = directory;
        return this;
    }

    @Override
    public ReplaceInFilesFromDirectoryTask addIncludePattern( final String pattern )
    {
        this.includes.add( pattern );
        return this;
    }

    @Override
    public ReplaceInFilesFromDirectoryTask addExcludePattern( final String pattern )
    {
        this.excludes.add( pattern );
        return this;
    }

    @Override
    public ReplaceInFilesFromDirectoryTask setFailIfDirectoryDoesNotExist( final boolean fail )
    {
        this.failIfDirectoryDoesNotExist = fail;
        return this;
    }

    private String merge( final List<String> strings )
    {
        final StringBuilder sb = new StringBuilder();
        for ( Object string : strings )
        {
            if ( sb.length() > 0 )
            {
                sb.append( "," );
            }
            sb.append( string );
        }
        return sb.toString();
    }

}
