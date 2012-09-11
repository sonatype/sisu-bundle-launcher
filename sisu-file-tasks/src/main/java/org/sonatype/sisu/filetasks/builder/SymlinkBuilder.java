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
import org.sonatype.sisu.filetasks.task.SymlinkTask;

/**
 * {@link SymlinkTask} builder.
 *
 * @since 1.0
 */
public interface SymlinkBuilder
    extends FileTask
{

    /**
     * Symlink to be created.
     *
     * @param as destination file/directory to be created as symlink
     * @return itself, for fluent API usage
     * @since 1.0
     */
    SymlinkBuilder as( FileRef as );

    /**
     * If an hard link should be created.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    SymlinkBuilder makeHardLink();

}
