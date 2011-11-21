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

import java.io.File;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * Creates a *NIX symbolic link.
 *
 * @since 1.0
 */
public interface SymlinkTask
    extends FileTask
{

    /**
     * Source file to be linked.
     *
     * @param file to be linked
     * @return itself, for fluent API usage
     * @since 1.0
     */
    SymlinkTask setSource( File file );

    /**
     * Target symlink to be created.
     *
     * @param file target
     * @return itself, for fluent API usage
     * @since 1.0
     */
    SymlinkTask setTarget( File file );

    /**
     * Whether a hard link should be created or not (default false).
     *
     * @param hardLink true, if a hard link should be created
     * @return itself, for fluent API usage
     * @since 1.0
     */
    SymlinkTask setHardLink(boolean hardLink );

}
