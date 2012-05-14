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

package org.sonatype.sisu.filetasks.builder;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A reference to a file. Reference could be a file or a re-target-able path.<br/>
 * Re-target-able paths are relative paths to a directory that can be specified before that path usage.
 *
 * @since 1.0
 */
public class FileRef
{

    /**
     * Concrete target file.
     * Never null.
     */
    protected File target;

    /**
     * Constructor.
     *
     * @param target target file.
     * @since 1.0
     */
    public FileRef( final File target )
    {
        this.target = checkNotNull( target );
    }

    /**
     * Returns referenced file.
     *
     * @return referenced file. Never null.
     * @since 1.0
     */
    public File getFile()
    {
        return target;
    }

    /**
     * Re-targets the reference to a specified directory.<br/>
     * If reference was created by {@link #file(java.io.File)}, referenced file will remain the same as prior invocation
     * .<br/>
     * If reference was created by {@link #path(String)}, referenced file will point to specified directory + specified
     * path.
     *
     * @param directory to re-target to
     * @return itself, for fluent APi usage
     * @since 1.0
     */
    public FileRef retarget( File directory )
    {
        return this;
    }

    /**
     * Creates a fixed reference to a file. Re-targeting will still reference same file as the parameter.
     *
     * @param target target file
     * @return reference to specified file. Never null.
     * @since 1.0
     */
    public static FileRef file( File target )
    {
        return new FileRef( target );
    }

    /**
     * Creates a relative path reference. Re-targeting will reference a file that is equal to specified target directory
     * plus path.
     *
     * @param path relative path
     * @return re-target-able path file reference. Never null.
     * @since 1.0
     */
    public static FileRef path( final String path )
    {
        return new FileRef( new File( path ) )
        {
            /**
             * Re-targets file to specified directory + path.
             *
             * {@inheritDoc}
             * @since 1.0
             */
            @Override
            public FileRef retarget( final File directory )
            {
                target = new File( directory, path );
                return this;
            }
        };
    }

}
