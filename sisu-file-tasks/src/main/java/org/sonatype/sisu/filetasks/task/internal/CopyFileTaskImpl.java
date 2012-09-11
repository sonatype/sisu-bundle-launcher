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
package org.sonatype.sisu.filetasks.task.internal;

import org.apache.tools.ant.taskdefs.Copy;
import org.sonatype.sisu.filetasks.task.CopyFileTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link CopyFileTask} implementation.
 *
 * @since 1.0
 */
@Named
class CopyFileTaskImpl
    extends AbstractCopyTask<CopyFileTaskImpl>
    implements CopyFileTask
{

    /**
     * File to be copied.
     */
    private File fromFile;

    /**
     * Target directory to copy to.
     */
    private File toDirectory;

    /**
     * Target file to copy to.
     */
    private File toFile;

    /**
     * True if copy should fail when file to be copied does not exist.
     */
    private boolean failIfSourceDoesNotExist;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    CopyFileTaskImpl()
    {
        failIfSourceDoesNotExist = true;
    }

    /**
     * Execute only when source file exists or it does not exist and we should fail.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    boolean shouldExecute()
    {
        return fromFile.exists() || failIfSourceDoesNotExist;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Copy copy )
    {
        super.prepare( copy );

        copy.setFile( checkNotNull( fromFile ) );
        if ( toDirectory != null )
        {
            copy.setTodir( toDirectory );
        }
        else
        {
            copy.setTofile( checkNotNull( toFile ) );
        }
        copy.setTodir( toDirectory );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyFileTask setFromFile( final File file )
    {
        this.fromFile = file;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyFileTask setToDirectory( final File directory )
    {
        this.toDirectory = directory;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyFileTask setToFile( final File file )
    {
        this.toFile = file;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyFileTask setFailIfSourceDoesNotExist( final boolean fail )
    {
        this.failIfSourceDoesNotExist = fail;
        return this;
    }

}
