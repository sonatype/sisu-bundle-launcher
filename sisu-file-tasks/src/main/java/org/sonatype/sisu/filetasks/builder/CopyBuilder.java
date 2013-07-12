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

/**
 * Ongoing copy file/directory builder.
 *
 * @since 1.0
 */
public interface CopyBuilder
{

    /**
     * {@link org.sonatype.sisu.filetasks.task.CopyDirectoryTask} builder.
     *
     * @param directory directory to be copied
     * @return ongoing copy directory task builder
     */
    CopyDirectoryBuilder directory( final FileRef directory );

    /**
     * {@link org.sonatype.sisu.filetasks.task.CopyFileTask} builder.
     *
     * @param file file to be copied
     * @return ongoing copy file task builder
     */
    CopyFileBuilder file( final FileRef file );

}
