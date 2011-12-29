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

import org.sonatype.sisu.filetasks.FileTask;

import java.io.File;
import java.util.Properties;

/**
 * {@link org.sonatype.sisu.filetasks.task.PropertiesTask} builder.
 *
 * @since 1.0
 */
public interface PropertiesBuilder
    extends FileTask
{

    /**
     * Sets a property by key. If property is not present it will be added.
     *
     * @param key   property key
     * @param value property value
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesBuilder property( String key, String value );

    /**
     * Sets/adds all properties specified.
     *
     * @param properties to be added
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesBuilder properties( Properties properties );

    /**
     * Reads properties from specified properties file and add/set them.
     *
     * @param propertiesFile file containing properties
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesBuilder properties( File propertiesFile );

    /**
     * Removes a property by key.
     *
     * @param key of property to be removed
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesBuilder removeProperty( String key );

    /**
     * Removes all properties. If there are properties to be set, those properties will replace all eventually existing
     * properties in target file.
     *
     * @return itself, for fluent API usage
     * @since 1.0
     */
    PropertiesBuilder removeAllProperties();

}
