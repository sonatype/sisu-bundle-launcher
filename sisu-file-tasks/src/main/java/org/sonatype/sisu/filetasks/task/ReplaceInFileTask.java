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
 * Replaces values in specified file.
 *
 * @since 1.3
 */
public interface ReplaceInFileTask
    extends ReplaceSettings
{

    /**
     * File in which replacements should be made.
     *
     * @param file to be deleted
     * @return itself, for fluent API usage
     */
    ReplaceInFileTask setFile( File file );

    /**
     * Whether or not replace should fail if file does not exist. By default it will not fail.
     *
     * @param fail true/false if replace should fail if file does not exist
     * @return itself, for fluent API usage
     */
    ReplaceInFileTask setFailIfFileDoesNotExist( boolean fail );

}
