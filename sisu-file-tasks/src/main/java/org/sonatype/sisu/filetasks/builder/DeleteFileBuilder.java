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
 * {@link org.sonatype.sisu.filetasks.task.DeleteFileTask} builder.
 *
 * @since 1.0
 */
public interface DeleteFileBuilder
    extends FileTask
{

    /**
     * Specifies that delete should fail if file to be removed does not exist.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteFileBuilder failIfNotPresent( );

    /**
     * Specifies that delete should not fail if file to be removed does not exist.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteFileBuilder doNotFailIfNotPresent( );

}
