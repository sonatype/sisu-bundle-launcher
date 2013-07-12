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
import java.util.Properties;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * Copy task settings.
 *
 * @see CopyDirectoryTask
 * @see CopyFileTask
 * @since 1.0
 */
public interface CopySettings
    extends FileTask
{

    /**
     * Whether or not newer files present in destination should be overwritten. By default it will be overwritten.
     *
     * @param overwrite true/false if newer files present in destination should be overwritten
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopySettings setOverwriteNewer( boolean overwrite );

    /**
     * Whether or not read only files present in destination should be overwritten. By default it will be overwritten.
     *
     * @param overwrite true/false if read only files present in destination should be overwritten
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopySettings setOverwriteReadOnly( boolean overwrite );

    /**
     * Adds a filtering property to be replaced in copied files.
     *
     * @param key   property key
     * @param value property value
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopySettings addFilter( String key, String value );

    /**
     * Adds all properties specified as filtering properties to be replaced in copied files.
     *
     * @param properties to add
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopySettings addFilters( Properties properties );

    /**
     * Reads all properties form specified properties file as filtering properties to be replaced in copied files.
     *
     * @param propertiesFile file containing properties
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopySettings addFilters( File propertiesFile );

    /**
     * Whether or not if copy should fail when file/directory to be copied does not exist. By default it will fail.
     *
     * @param fail true/false if copy should fail when file/directory to be copied does not exist
     * @return itself, for fluent API usage
     * @since 1.0
     */
    CopySettings setFailIfSourceDoesNotExist( boolean fail );

}
