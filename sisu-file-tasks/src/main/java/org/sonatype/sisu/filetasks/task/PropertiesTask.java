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

import org.sonatype.sisu.filetasks.FileTask;

import java.io.File;
import java.util.Properties;

/**
 * Creates/modifies a properties file.
 *
 * @since 1.0
 */
public interface PropertiesTask
    extends FileTask
{

    /**
     * Properties file to be modified. If file does not exist it will be created.
     *
     * @param propertiesFile to be created/modified
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesTask setTargetFile( File propertiesFile );

    /**
     * Sets a property by key. If property is not present it will be added.
     *
     * @param key   property key
     * @param value property value
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesTask setProperty( String key, String value );

    /**
     * Sets/adds all properties specified.
     *
     * @param properties to be added
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesTask setProperties( Properties properties );

    /**
     * Reads properties from specified properties file and add/set them.
     *
     * @param propertiesFile file containing properties
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesTask setProperties( File propertiesFile );

    /**
     * Removes a property by key.
     *
     * @param key of property to be removed
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesTask removeProperty( String key );

    /**
     * Removes all properties. If there are properties to be set, those properties will replace all eventually existing
     * properties in target file.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesTask removeAllProperties();

}
