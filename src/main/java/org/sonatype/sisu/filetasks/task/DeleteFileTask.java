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
 * Deletes a file.
 *
 * @since 1.0
 */
public interface DeleteFileTask
    extends FileTask
{

    /**
     * File to be deleted.
     *
     * @param file to be deleted
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteFileTask setFile( File file );

    /**
     * Whether or not delete should fail if file to be removed does not exist. By default it will not fail.
     *
     * @param failIfNotPresent true/false if delete should fail if file to be removed does not exist
     * @return itself, for fluent API usage
     * @since 1.0
     */
    DeleteFileTask setFailIfNotPresent( boolean failIfNotPresent );

}
