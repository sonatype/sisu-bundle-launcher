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

package org.sonatype.sisu.filetasks.task;

import org.sonatype.sisu.filetasks.FileTask;

import java.io.File;

/**
 * Copies a directory to a target directory, filtering based on include/exclude patterns.
 *
 * @since 1.0
 */
public interface CopyDirectoryTask
    extends CopySettings, FileTask
{

    /**
     * Source directory to be copied.
     *
     * @param directory to be copied
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryTask setFromDirectory( File directory );

    /**
     * Target directory where the files should be copied.
     *
     * @param directory target directory where the files should be copied
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryTask setToDirectory( File directory );

    /**
     * Adds an include pattern (ANT style) to filter the files to be copied.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryTask addIncludePattern( String pattern );

    /**
     * Adds an exclude pattern (ANT style) to filter the files to be copied.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryTask addExcludePattern( String pattern );

    /**
     * Whether or not empty directories should be copied. By default empty directories will be copied.
     *
     * @param includeEmptyDirectories true/false if empty directories should be copied
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryTask setIncludeEmptyDirectories( boolean includeEmptyDirectories );

}
