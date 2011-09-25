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

package org.sonatype.sisu.filetasks;

import org.sonatype.sisu.filetasks.builder.Targetable;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * Helper for execution of {@link FileTask}s.
 *
 * @since 1.0
 */
public class FileTaskRunner
{

    /**
     * Ongoing builder for specifying a set of tasks that should be applied over specified directory.
     *
     * @param directory to which {@link FileTask}s should be applied
     * @return ongoing builder
     * @since 1.0
     */
    public static Target onDirectory( final File directory )
    {
        return new Target()
        {

            @Override
            public void apply( final FileTask... tasks )
            {
                if ( tasks != null && tasks.length > 0 )
                {
                    apply( Arrays.asList( tasks ) );
                }
            }

            @Override
            public void apply( final Collection<FileTask> tasks )
            {
                if ( tasks != null && tasks.size() > 0 )
                {
                    for ( FileTask task : tasks )
                    {
                        if ( task instanceof Targetable )
                        {
                            ( (Targetable) task ).setTargetDirectory( directory );
                        }
                        task.run();
                    }
                }
            }

        };
    }

    /**
     * Target set of {@link FileTask}s.
     *
     * @since 1.0
     */
    public static interface Target
    {

        /**
         * Executes the specified tasks.
         *
         * @param tasks to be executed
         * @since 1.0
         */
        void apply( FileTask... tasks );

        /**
         * Executes the specified tasks.
         *
         * @param tasks to be executed
         * @since 1.0
         */
        void apply( Collection<FileTask> tasks );

    }

}
