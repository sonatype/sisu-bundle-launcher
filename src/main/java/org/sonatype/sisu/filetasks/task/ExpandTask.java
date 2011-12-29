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
 * Expands an archive.
 *
 * @since 1.0
 */
public interface ExpandTask
    extends FileTask
{

    /**
     * Archive to be expanded. It can be in any of zip/jar/tar formats (format determined by extension).
     *
     * @param archive to be expanded
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ExpandTask setArchive( File archive );

    /**
     * Target directory where archive will be expanded.
     *
     * @param directory where archive will be expanded
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ExpandTask setToDirectory( File directory );

    /**
     * Whether or not newer files present in destination should be overwritten.
     *
     * @param overwrite true/false newer files present in destination should be overwritten
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ExpandTask setOverwriteNewer( boolean overwrite );

    /**
     * Number of directories to be cut (form source archive), while expanding.
     *
     * @param directoriesToCut number of directories to be cut (form source archive), while expanding
     * @return itself, for fluent API usage
     * @since 1.0
     */
    ExpandTask setDirectoriesToCut( int directoriesToCut );

}
