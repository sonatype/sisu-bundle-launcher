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
package org.sonatype.sisu.filetasks.task;

import java.io.File;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * Replaces values in all files from a directory.
 *
 * @since 1.3
 */
public interface ReplaceInFilesFromDirectoryTask
    extends ReplaceSettings
{

    /**
     * Directory containing files in which replacements should be made.
     *
     * @param directory containing files in which replacements should be made
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryTask setDirectory( File directory );

    /**
     * Adds an include pattern (ANT style) to filter the files in which replacements should be made.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryTask addIncludePattern( String pattern );

    /**
     * Adds an exclude pattern (ANT style) to filter the files in which replacements should be made.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryTask addExcludePattern( String pattern );

    /**
     * Whether or not replace should fail when directory does not exist.
     *
     * @param fail true/false if replace should fail when directory does not exist
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryTask setFailIfDirectoryDoesNotExist( boolean fail );

}
