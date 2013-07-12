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
package org.sonatype.sisu.filetasks.task.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Properties file related utility methods.
 *
 * @since 1.0
 */
public class PropertiesHelper
{

    /**
     * Loads a properties file. If file does not exist creates a set of empty properties.
     *
     * @param propertiesFile properties file to be loaded
     * @return loaded properties, empty if properties file does not exist
     */
    public static Properties load( File propertiesFile )
    {
        if ( !checkNotNull( propertiesFile ).exists() )
        {
            return new Properties();
        }
        InputStream in = null;
        try
        {
            in = new BufferedInputStream( new FileInputStream( propertiesFile ) );
            Properties properties = new Properties();
            if ( propertiesFile.getName().endsWith( ".xml" ) )
            {
                properties.loadFromXML( in );
            }
            else
            {
                properties.load( in );
            }
            return properties;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            if ( in != null )
            {
                try
                {
                    in.close();
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( e );
                }
            }
        }
    }

    /**
     * Saves properties to a file.
     *
     * @param properties     to be saved
     * @param propertiesFile destination properties file
     */
    public static void save( Properties properties, File propertiesFile )
    {
        OutputStream out = null;
        try
        {
            checkNotNull( propertiesFile ).getParentFile().mkdirs();
            out = new BufferedOutputStream( new FileOutputStream( propertiesFile ) );
            if ( propertiesFile.getName().endsWith( ".xml" ) )
            {
                properties.storeToXML( out, null );
            }
            else
            {
                properties.store( out, null );
            }
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            if ( out != null )
            {
                try
                {
                    out.close();
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( e );
                }
            }
        }
    }

}
