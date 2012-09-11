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

/**
 * Ongoing replace file/directory builder.
 *
 * @since 1.3
 */
public interface ReplaceBuilder
{

    /**
     * {@link ReplaceInFilesFromDirectoryBuilder}.
     *
     * @param directory directory containing files in which replacements should be made
     * @return ongoing replace in files from directory task builder
     */
    ReplaceInFilesFromDirectoryBuilder inFilesFromDirectory( final FileRef directory );

    /**
     * {@link ReplaceInFileBuilder}.
     *
     * @param file file in which replacements should be made
     * @return ongoing replace in file task builder
     */
    ReplaceInFileBuilder inFile( final FileRef file );

}
