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

import org.sonatype.sisu.filetasks.builder.ChmodBuilder;
import org.sonatype.sisu.filetasks.builder.CopyBuilder;
import org.sonatype.sisu.filetasks.builder.DeleteBuilder;
import org.sonatype.sisu.filetasks.builder.ExpandBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.MoveBuilder;
import org.sonatype.sisu.filetasks.builder.PropertiesBuilder;
import org.sonatype.sisu.filetasks.builder.RenameBuilder;

/**
 * Helper for easy, fluent API building of {@link FileTask}s.
 *
 * @since 1.0
 */
public interface FileTaskBuilder
{

    /**
     * {@link org.sonatype.sisu.filetasks.task.CopyDirectoryTask} or {@link org.sonatype.sisu.filetasks.task.CopyFileTask}
     * builder.
     *
     * @return ongoing copy task builder
     * @since 1.0
     */
    CopyBuilder copy();

    /**
     * {@link org.sonatype.sisu.filetasks.task.DeleteDirectoryTask} or {@link org.sonatype.sisu.filetasks.task.DeleteFileTask}
     * builder.
     *
     * @return ongoing delete task builder
     * @since 1.0
     */
    DeleteBuilder delete();

    /**
     * {@link org.sonatype.sisu.filetasks.task.RenameTask} builder.
     *
     * @param target renamed file/directory reference
     * @return ongoing rename task builder
     * @since 1.0
     */
    RenameBuilder rename( FileRef target );

    /**
     * {@link org.sonatype.sisu.filetasks.task.MoveTask} builder.
     *
     * @param from file to be moved reference
     * @return ongoing move task builder
     * @since 1.0
     */
    MoveBuilder move( FileRef from );

    /**
     * {@link org.sonatype.sisu.filetasks.task.ExpandTask} builder.
     *
     * @param archive archive to be expanded reference
     * @return ongoing expand task builder
     * @since 1.0
     */
    ExpandBuilder expand( FileRef archive );

    /**
     * {@link org.sonatype.sisu.filetasks.task.PropertiesTask} builder.
     *
     * @param target properties file to be changed/created reference
     * @return ongoing properties task builder
     * @since 1.0
     */
    PropertiesBuilder properties( FileRef target );

    /**
     * {@link org.sonatype.sisu.filetasks.task.ChmodTask} builder.
     *
     * @param directory directory containing files to have permissions changed reference
     * @return ongoing change permissions task builder
     * @since 1.0
     */
    ChmodBuilder chmod( FileRef directory );

}
