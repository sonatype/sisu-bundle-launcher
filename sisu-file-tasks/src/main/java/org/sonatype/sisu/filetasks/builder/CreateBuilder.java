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
 * Ongoing create file/directory builder.
 *
 * @since 1.0
 */
public interface CreateBuilder {

    /**
     * {@link org.sonatype.sisu.filetasks.task.CreateFileTask} builder.
     *
     * @param file file to be created
     * @return ongoing create file task builder
     */
    CreateFileBuilder file( final FileRef file );

    /**
     * {@link org.sonatype.sisu.filetasks.task.CreateDirectoryTask} builder.
     *
     * @param dir directory to be created
     * @return ongoing create directory task builder
     */
    CreateDirectoryBuilder directory( final FileRef file );


}
