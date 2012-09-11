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


import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

import org.apache.tools.ant.taskdefs.Mkdir;
import org.sonatype.sisu.filetasks.task.CreateDirectoryTask;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link CreateDirectoryTask} implementation.
 *
 * @since 1.0
 */
@Named
class CreateDirectoryTaskImpl
    extends AbstractAntTask<Mkdir>
    implements CreateDirectoryTask
{

    private File dirToCreate;

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CreateDirectoryTask setDirectory( final File file )
    {
        this.dirToCreate = file;
        return this;
    }

    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    CreateDirectoryTaskImpl()
    {
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Mkdir mkdir )
    {
        mkdir.setDir( checkNotNull( dirToCreate ) );
    }


    @Override
    Class antTaskType()
    {
        return Mkdir.class;
    }


}
