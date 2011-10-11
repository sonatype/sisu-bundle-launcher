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
 * Task to create a file with some content.
 *
 * @since 1.0
 */
public interface CreateFileTask
    extends FileTask
{
    /**
     * Set the content the new file should contain.
     * @param content
     * @return CreateFileTask for fluent API
     */
    CreateFileTask setContent(String content);

    /**
     * Set the encoding the content should be written into the new file with.
     * <p>
     * If not specified content should be written with default system encoding.
     *
     * @param encoding the encoding to write content with
     * @return CreateFileTask for fluent API
     */
    CreateFileTask setEncoding(String encoding);

    /**
     * Set the file to create
     *
     * @param fileToCreate the file to create
     * @return CreateFileTask for fluent API
     */
    CreateFileTask setFile(File fileToCreate);
}
