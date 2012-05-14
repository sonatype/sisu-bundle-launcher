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

package org.sonatype.sisu.filetasks.builder;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * {@link org.sonatype.sisu.filetasks.task.DeleteDirectoryTask} builder.
 *
 * @since 1.0
 */
public interface DeleteDirectoryBuilder
    extends FileTask
{

    /**
     * Adds an include pattern (ANT style) to filter the files to be deleted.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryBuilder include( String pattern );

    /**
     * Adds an exclude pattern (ANT style) to filter the files to be deleted.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryBuilder exclude( String pattern );

    /**
     * Specifies that once a directory is empty (or was empty before delete), it should also be deleted.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryBuilder includeEmptyDirectories();

    /**
     * Specifies that once a directory is empty (or was empty before delete), it should not be deleted.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryBuilder excludeEmptyDirectories();

    /**
     * Specifies that delete should fail if directory to be removed does not exist.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryBuilder failIfNotPresent( );

    /**
     * Specifies that delete should not fail if directory to be removed does not exist.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryBuilder doNotFailIfNotPresent( );

}
