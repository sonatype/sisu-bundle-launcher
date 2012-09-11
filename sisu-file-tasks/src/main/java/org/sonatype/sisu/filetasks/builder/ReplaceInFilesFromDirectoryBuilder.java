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
package org.sonatype.sisu.filetasks.builder;

import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.task.ReplaceInFilesFromDirectoryTask;

/**
 * {@link ReplaceInFilesFromDirectoryTask} builder.
 *
 * @since 1.3
 */
public interface ReplaceInFilesFromDirectoryBuilder
    extends ReplaceSettingsBuilder<ReplaceInFilesFromDirectoryBuilder>, FileTask
{

    /**
     * Adds an include pattern (ANT style) to filter the files in which replacements should be made.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryBuilder include( String pattern );

    /**
     * Adds an exclude pattern (ANT style) to filter the files in which replacements should be made.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryBuilder exclude( String pattern );

    /**
     * Specifies that replace should fail if directory does not exist.
     *
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryBuilder failIfDirectoryDoesNotExist();

    /**
     * Specifies that replace should not fail if directory does not exist.
     *
     * @return itself, for fluent API usage
     */
    ReplaceInFilesFromDirectoryBuilder doNotFailIfDirectoryDoesNotExist();

}
