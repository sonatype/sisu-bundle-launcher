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

import org.apache.tools.ant.taskdefs.Chmod;
import org.apache.tools.ant.types.FileSet;
import org.sonatype.sisu.filetasks.task.ChmodTask;

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
class ChmodTaskImpl
    extends AbstractAntTask<Chmod>
    implements ChmodTask
{

    private File directory;

    private final List<String> includes;

    private final List<String> excludes;

    private String permissions;

    @Inject
    ChmodTaskImpl()
    {
        includes = new ArrayList<String>();
        excludes = new ArrayList<String>();
    }

    @Override
    Class<Chmod> antTaskType()
    {
        return Chmod.class;
    }

    @Override
    void prepare( final Chmod chmod )
    {
        FileSet fileSet = new FileSet();
        fileSet.setDir( checkNotNull( directory ) );
        if ( includes.size() > 0 )
        {
            fileSet.appendIncludes( includes.toArray( new String[includes.size()] ) );
        }
        if ( excludes.size() > 0 )
        {
            fileSet.appendExcludes( excludes.toArray( new String[excludes.size()] ) );
        }
        chmod.addFileset( fileSet );
        chmod.setPerm( checkNotNull( permissions ) );
    }

    @Override
    public ChmodTaskImpl setDirectory( final File directory )
    {
        this.directory = checkNotNull( directory );
        return this;
    }

    @Override
    public ChmodTaskImpl addIncludePattern( final String pattern )
    {
        this.includes.add( checkNotNull( pattern ) );
        return this;
    }

    @Override
    public ChmodTaskImpl addExcludePattern( final String pattern )
    {
        this.excludes.add( checkNotNull( pattern ) );
        return this;
    }

    @Override
    public ChmodTask setPermissions( final String permissions )
    {
        this.permissions = checkNotNull( permissions );
        return this;
    }

}
