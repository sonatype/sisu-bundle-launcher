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
 * Changes permissions of files from a directory.
 *
 * @since 1.0
 */
public interface ChmodTask
    extends FileTask
{

    /**
     * Specify the directory containing the files to have permission changed.
     *
     * @param directory directory containing the files to have permissions changed
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ChmodTask setDirectory( File directory );

    /**
     * Adds an include pattern (ANT style) to filter the files to have permissions changed.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ChmodTask addIncludePattern( String pattern );

    /**
     * Adds an exclude pattern (ANT style) to filter the files to have permissions changed.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ChmodTask addExcludePattern( String pattern );

    /**
     * Permissions to be applied to files form specified directory that matches include/exclude patterns.
     *
     * @param permissions linus style permissions to be applied
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ChmodTask setPermissions( String permissions );

}
