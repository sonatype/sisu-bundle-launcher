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
 * ANT based {@link ChmodTask} implementation.
 *
 * @since 1.0
 */
@Named
class ChmodTaskImpl
    extends AbstractAntTask<Chmod>
    implements ChmodTask
{

    /**
     * Directory containing files to change permissions for.
     */
    private File directory;

    /**
     * Include patterns to filter files that will have permissions changed.
     * Never null.
     */
    private final List<String> includes;

    /**
     * Exclude patterns to filter files that will have permissions changed.
     * Never null.
     */
    private final List<String> excludes;

    /**
     * Permissions that should be set.
     */
    private String permissions;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    ChmodTaskImpl()
    {
        includes = new ArrayList<String>();
        excludes = new ArrayList<String>();
    }

    /**
     * Returns a {@link Chmod} ANT task.
     * 
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Class<Chmod> antTaskType()
    {
        return Chmod.class;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
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

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodTaskImpl setDirectory( final File directory )
    {
        this.directory = checkNotNull( directory );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodTaskImpl addIncludePattern( final String pattern )
    {
        this.includes.add( checkNotNull( pattern ) );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodTaskImpl addExcludePattern( final String pattern )
    {
        this.excludes.add( checkNotNull( pattern ) );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodTask setPermissions( final String permissions )
    {
        this.permissions = checkNotNull( permissions );
        return this;
    }

}
