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
package org.sonatype.sisu.filetasks.builder;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * {@link org.sonatype.sisu.filetasks.task.CopyDirectoryTask} builder.
 *
 * @since 1.0
 */
public interface CopyDirectoryBuilder
    extends CopySettingsBuilder<CopyDirectoryBuilder>, FileTask
{

    /**
     * Adds an include pattern (ANT style) to filter the files to be copied.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryBuilder include( String pattern );

    /**
     * Adds an exclude pattern (ANT style) to filter the files to be copied.
     *
     * @param pattern ANT style file pattern
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryBuilder exclude( String pattern );

    /**
     * Specifies that empty directories should be copied.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryBuilder includeEmptyDirectories();

    /**
     * Specifies that empty directories should not be copied.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopyDirectoryBuilder excludeEmptyDirectories();

    /**
     * Ongoing destination builder.
     *
     * @return ongoing destination builder.
     * @since 1.0
     */
    DestinationBuilder to();

    /**
     * Ongoing destination builder.
     *
     * @since 1.0
     */
    interface DestinationBuilder
    {

        /**
         * Target directory where the files should be copied.
         *
         * @param directory target directory where the files should be copied
         * @return itself, for fluent API usage
         * @since 1.0
         */
        CopyDirectoryBuilder directory( FileRef directory );

    }

}
