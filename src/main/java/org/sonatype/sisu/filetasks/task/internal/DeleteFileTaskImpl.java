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
import org.sonatype.sisu.filetasks.task.DeleteFileTask;

import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link DeleteFileTask} implementation.
 *
 * @since 1.0
 */
@Named
class DeleteFileTaskImpl
    extends AbstractAntTask<Delete>
    implements DeleteFileTask
{

    /**
     * File to be deleted.
     */
    private File file;

    /**
     * True if delete should fail if file to be deleted does not exist.
     */
    private boolean failIfNotPresent;

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
     * Returns false if file to be deleted does not exist, true otherwise.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    boolean shouldExecute()
    {
        return failIfNotPresent || file.exists();
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
        fileSet.setFile( checkNotNull( file ) );
        delete.addFileset( fileSet );
        delete.setFailOnError( true );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteFileTaskImpl setFile( final File file )
    {
        this.file = file;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteFileTaskImpl setFailIfNotPresent( final boolean failIfNotPresent )
    {
        this.failIfNotPresent = failIfNotPresent;
        return this;
    }

}
