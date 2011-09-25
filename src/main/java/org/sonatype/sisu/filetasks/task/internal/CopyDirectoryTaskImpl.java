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

package org.sonatype.sisu.filetasks.task.internal;

import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.sonatype.sisu.filetasks.task.CopyDirectoryTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class CopyDirectoryTaskImpl
    extends AbstractCopyTask<CopyDirectoryTaskImpl>
    implements CopyDirectoryTask
{

    private File fromDirectory;

    private File toDirectory;

    private boolean includeEmptyDirectories;

    private final List<String> includes;

    private final List<String> excludes;

    @Inject
    CopyDirectoryTaskImpl()
    {
        includes = new ArrayList<String>();
        excludes = new ArrayList<String>();
    }

    @Override
    void prepare( final Copy copy )
    {
        super.prepare( copy );

        FileSet fileSet = new FileSet();
        fileSet.setDir( checkNotNull( fromDirectory ) );
        if ( includes.size() > 0 )
        {
            fileSet.appendIncludes( includes.toArray( new String[includes.size()] ) );
        }
        if ( excludes.size() > 0 )
        {
            fileSet.appendExcludes( excludes.toArray( new String[excludes.size()] ) );
        }
        copy.addFileset( fileSet );
        copy.setIncludeEmptyDirs( includeEmptyDirectories );
        copy.setTodir( toDirectory );
    }

    @Override
    public CopyDirectoryTask setFromDirectory( final File directory )
    {
        this.fromDirectory = directory;
        return this;
    }

    public CopyDirectoryTask setToDirectory( final File directory )
    {
        this.toDirectory = directory;
        return this;
    }

    @Override
    public CopyDirectoryTask setIncludeEmptyDirectories( final boolean includeEmptyDirectories )
    {
        this.includeEmptyDirectories = includeEmptyDirectories;
        return this;
    }

    @Override
    public CopyDirectoryTaskImpl addIncludePattern( final String pattern )
    {
        this.includes.add( pattern );
        return this;
    }

    @Override
    public CopyDirectoryTaskImpl addExcludePattern( final String pattern )
    {
        this.excludes.add( pattern );
        return this;
    }

}
