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

import org.apache.tools.ant.Task;
import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.support.AntHelper;

import javax.inject.Inject;

/**
 * TODO
 *
 * @since 1.0
 */
abstract class AbstractAntTask<T extends Task>
    implements FileTask
{

    @Inject
    private AntHelper ant;

    @Override
    public final void run()
    {
        T antTask = ant().createTask( antTaskType() );
        if ( shouldExecute() )
        {
            prepare( antTask );
            antTask.execute();
        }
    }

    boolean shouldExecute()
    {
        return true;
    }

    abstract Class<? extends T> antTaskType();

    abstract void prepare( T antTask );

    AntHelper ant()
    {
        return ant;
    }
}
