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

import org.apache.tools.ant.Task;
import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.support.AntHelper;

import javax.inject.Inject;

/**
 * Base class for all ANT based tasks implementations.
 *
 * @since 1.0
 */
abstract class AbstractAntTask<T extends Task>
    implements FileTask
{

    /**
     * ANT helper used to create ANT tasks.
     */
    @Inject
    private AntHelper ant;

    /**
     * Creates an ANT task (type specified by subclasses), asks to be prepared and executes it if
     * {@link #shouldExecute()} (default, if not overridden returns true)
     *
     * @since 1.0
     */
    @Override
    public final void run()
    {
        if ( shouldExecute() )
        {
            T antTask = ant.createTask( antTaskType() );
            prepare( antTask );
            antTask.execute();
        }
    }

    /**
     * Whether or not the task should be executed. For example a delete task should not be executed if file to be
     * deleted does not exist.
     *
     * @return if task should be executed (returns true by default)
     * @since 1.0
     */
    boolean shouldExecute()
    {
        return true;
    }

    /**
     * Type of ANT task to be created.
     * Must be implemented by concrete file tasks.
     *
     * @return type of ANT task to be created
     * @since 1.0
     */
    abstract Class<? extends T> antTaskType();

    /**
     * Prepares the ANT task before execution by setting available options/values.
     * Must be implemented by concrete file tasks.
     *
     * @param antTask to be prepared before is executed
     * @since 1.0
     */
    abstract void prepare( T antTask );

}
