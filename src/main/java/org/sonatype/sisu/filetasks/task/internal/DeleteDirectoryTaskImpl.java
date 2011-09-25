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

import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;
import org.sonatype.sisu.filetasks.task.DeleteDirectoryTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class DeleteDirectoryTaskImpl
    extends AbstractAntTask<Delete>
    implements DeleteDirectoryTask
{

    private File directory;

    private boolean includeEmptyDirectories;

    private final List<String> includes;

    private final List<String> excludes;

    @Inject
    DeleteDirectoryTaskImpl()
    {
        includes = new ArrayList<String>();
        excludes = new ArrayList<String>();
        includeEmptyDirectories = true;
    }

    @Override
    Class<Delete> antTaskType()
    {
        return Delete.class;
    }

    @Override
    boolean shouldExecute()
    {
        return directory.exists();
    }

    @Override
    void prepare( final Delete delete )
    {
        FileSet fileSet = new FileSet();
        fileSet.setDir( directory );
        if ( includes.size() > 0 )
        {
            fileSet.appendIncludes( includes.toArray( new String[includes.size()] ) );
        }
        if ( excludes.size() > 0 )
        {
            fileSet.appendExcludes( excludes.toArray( new String[excludes.size()] ) );
        }
        delete.addFileset( fileSet );
        delete.setIncludeEmptyDirs( includeEmptyDirectories );
        delete.setFailOnError( true );
    }

    @Override
    public DeleteDirectoryTaskImpl setDirectory( final File directory )
    {
        this.directory = directory;
        return this;
    }

    @Override
    public DeleteDirectoryTaskImpl setIncludeEmptyDirectories( final boolean includeEmptyDirectories )
    {
        this.includeEmptyDirectories = includeEmptyDirectories;
        return this;
    }

    @Override
    public DeleteDirectoryTaskImpl addIncludePattern( final String pattern )
    {
        this.includes.add( pattern );
        return this;
    }

    @Override
    public DeleteDirectoryTaskImpl addExcludePattern( final String pattern )
    {
        this.excludes.add( pattern );
        return this;
    }

}
