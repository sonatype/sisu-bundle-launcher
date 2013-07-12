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
 * Ongoing delete file/directory builder.
 *
 * @since 1.0
 */
public interface DeleteBuilder
{

    /**
     * {@link org.sonatype.sisu.filetasks.task.DeleteDirectoryTask} builder.
     *
     * @param directory directory to be deleted
     * @return ongoing delete directory task builder
     */
    DeleteDirectoryBuilder directory( final FileRef directory );

    /**
     * {@link org.sonatype.sisu.filetasks.task.DeleteFileTask} builder.
     *
     * @param file file to be deleted
     * @return ongoing delete file task builder
     */
    DeleteFileBuilder file( final FileRef file );

}
