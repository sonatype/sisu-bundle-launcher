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

import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;
import org.sonatype.sisu.filetasks.task.DeleteDirectoryTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link DeleteDirectoryTask} implementation.
 *
 * @since 1.0
 */
@Named
class DeleteDirectoryTaskImpl
    extends AbstractAntTask<Delete>
    implements DeleteDirectoryTask
{

    /**
     * Directory to be deleted.
     */
    private File directory;

    /**
     * True if empty or becoming empty directories should be deleted. Default true.
     */
    private boolean includeEmptyDirectories;

    /**
     * Include patterns to filter files that will be deleted.
     * Never null.
     */
    private final List<String> includes;

    /**
     * Exclude patterns to filter files that will be deleted.
     * Never null.
     */
    private final List<String> excludes;

    /**
     * True if delete should fail if file to be deleted does not exist.
     */
    private boolean failIfNotPresent;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    DeleteDirectoryTaskImpl()
    {
        includes = new ArrayList<String>();
        excludes = new ArrayList<String>();
        includeEmptyDirectories = true;
    }

    /**
     * Returns a {@link Delete} ANT task.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Class<Delete> antTaskType()
    {
        return Delete.class;
    }

    /**
     * Returns false if directory to be deleted does not exist, true otherwise.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    boolean shouldExecute()
    {
        return failIfNotPresent || directory.exists();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Delete delete )
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
        delete.addFileset( fileSet );
        delete.setIncludeEmptyDirs( includeEmptyDirectories );
        delete.setFailOnError( true );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryTaskImpl setDirectory( final File directory )
    {
        this.directory = directory;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryTaskImpl setIncludeEmptyDirectories( final boolean includeEmptyDirectories )
    {
        this.includeEmptyDirectories = includeEmptyDirectories;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryTaskImpl addIncludePattern( final String pattern )
    {
        this.includes.add( pattern );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryTaskImpl addExcludePattern( final String pattern )
    {
        this.excludes.add( pattern );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteDirectoryTaskImpl setFailIfNotPresent( final boolean failIfNotPresent )
    {
        this.failIfNotPresent = failIfNotPresent;
        return this;
    }

}
