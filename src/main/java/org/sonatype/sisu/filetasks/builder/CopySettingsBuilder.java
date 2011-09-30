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
import java.util.Properties;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * Copy settings builder (included in {@link CopyFileBuilder} and {@link CopyDirectoryBuilder}.
 *
 * @since 1.0
 */
public interface CopySettingsBuilder<B extends CopySettingsBuilder>
    extends FileTask
{

    /**
     * Specifies that newer files present in destination should be overwritten.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B overwriteNewer();

    /**
     * Specifies that newer files present in destination should not be overwritten.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B doNotOverwriteNewer();

    /**
     * Specifies that read only files present in destination should be overwritten.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B overwriteReadOnly();

    /**
     * Specifies that read only files present in destination should not be overwritten.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B doNotOverwriteReadOnly();

    /**
     * Adds a filtering property to be replaced in copied files.
     *
     * @param key   property key
     * @param value property value
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B filterUsing( String key, String value );

    /**
     * Adds all properties specified as filtering properties to be replaced in copied files.
     *
     * @param properties to add
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B filterUsing( Properties properties );

    /**
     * Reads all properties form specified properties file as filtering properties to be replaced in copied files.
     *
     * @param propertiesFile file containing properties
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B filterUsing( File propertiesFile );

    /**
     * Specifies that copy should fail when file/directory to be copied does not exist.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B failIfSourceDoesNotExist();

    /**
     * Specifies that copy should not fail when file/directory to be copied does not exist.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    B doNotFailIfSourceDoesNotExist();

}
