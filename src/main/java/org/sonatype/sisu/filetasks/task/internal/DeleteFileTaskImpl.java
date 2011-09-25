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

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class DeleteFileTaskImpl
    extends AbstractAntTask<Delete>
    implements DeleteFileTask
{

    private File file;

    @Override
    Class<Delete> antTaskType()
    {
        return Delete.class;
    }

    @Override
    boolean shouldExecute()
    {
        return file.exists();
    }

    @Override
    void prepare( final Delete delete )
    {
        FileSet fileSet = new FileSet();
        fileSet.setFile( file );
        delete.addFileset( fileSet );
        delete.setFailOnError( true );
    }

    @Override
    public DeleteFileTaskImpl setFile( final File file )
    {
        this.file = file;
        return this;
    }

}
