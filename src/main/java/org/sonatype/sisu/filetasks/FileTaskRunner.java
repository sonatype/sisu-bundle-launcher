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
 * TODO
 *
 * @since 1.0
 */
public class FileTaskRunner {

    public static Target onDirectory(final File directory) {
        return new Target() {
            @Override
            public void apply(final FileTask... tasks) {
                if (tasks != null && tasks.length > 0) {
                    apply(Arrays.asList(tasks));
                }
            }

            @Override
            public void apply(final Collection<FileTask> tasks) {
                if (tasks != null && tasks.size() > 0) {
                    for (FileTask task : tasks) {
                        if (task instanceof Targetable) {
                            ((Targetable) task).setTargetDirectory(directory);
                        }
                        task.run();
                    }
                }
            }
        };
    }

    public static interface Target {

        void apply(FileTask... tasks);

        void apply(Collection<FileTask> tasks);
    }

}
