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
package org.sonatype.sisu.filetasks.task;

import org.sonatype.sisu.filetasks.FileTask;

import java.io.File;

/**
 * Recursively deletes a directory.
 *
 * @since 1.0
 */
public interface DeleteDirectoryTask
    extends FileTask
{

    /**
     * Directory to be deleted.
     *
     * @param directory to be deleted
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryTask setDirectory( File directory );

    /**
     * Adds an include pattern (ANT style) to filter the files to be deleted.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryTask addIncludePattern( String pattern );

    /**
     * Adds an exclude pattern (ANT style) to filter the files to be deleted.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryTask addExcludePattern( String pattern );

    /**
     * Whether or not once a directory is empty (or was empty before delete), it should also be deleted.
     *
     * @param includeEmptyDirectories true/false if once a directory is empty (or was empty before delete), it should
     *                                also be deleted
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryTask setIncludeEmptyDirectories( boolean includeEmptyDirectories );

    /**
     * Whether or not delete should fail if file to be removed does not exist. By default it will not fail.
     *
     * @param failIfNotPresent true/false if delete should fail if file to be removed does not exist
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteDirectoryTask setFailIfNotPresent( boolean failIfNotPresent );

}
