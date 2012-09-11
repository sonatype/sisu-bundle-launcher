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

import java.io.File;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.tools.ant.taskdefs.Replace;
import org.sonatype.sisu.filetasks.task.ReplaceInFileTask;

/**
 * ANT based {@link ReplaceInFileTask} implementation.
 *
 * @since 1.3
 */
@Named
class ReplaceInFileTaskImpl
    extends AbstractReplaceTask<ReplaceInFileTaskImpl>
    implements ReplaceInFileTask
{

    /**
     * File in which replacements should be made.
     */
    private File file;

    /**
     * True if replace should fail if file does not exist.
     */
    private boolean failIfFileDoesNotExist;

    @Inject
    ReplaceInFileTaskImpl()
    {
        failIfFileDoesNotExist = true;
    }

    /**
     * Returns false if file does not exist, true otherwise.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    boolean shouldExecute()
    {
        return super.shouldExecute() && ( failIfFileDoesNotExist || file.exists() );
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
        task.setFile( file );
    }

    @Override
    public ReplaceInFileTaskImpl setFile( final File file )
    {
        this.file = file;
        return this;
    }

    @Override
    public ReplaceInFileTask setFailIfFileDoesNotExist( final boolean fail )
    {
        this.failIfFileDoesNotExist = fail;
        return this;
    }

}
