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

package org.sonatype.sisu.jsw.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * TODO
 *
 * @since 1.0
 */
public class JSWConfig
{

    private File config;

    private File override;

    private Properties configProperties;

    private Properties overrideProperties;

    private Properties combinedProperties;

    public JSWConfig( final File config, final File override )
    {
        this.config = config;
        this.override = override;
        configProperties = new Properties();
        overrideProperties = new Properties();
        combinedProperties = new Properties( configProperties );
    }

    public JSWConfig load()
        throws IOException
    {
        {
            InputStream in = null;
            try
            {
                in = new BufferedInputStream( new FileInputStream( config ) );
                configProperties.load( in );
            }
            finally
            {
                if ( in != null )
                {
                    in.close();
                }
            }
        }

        if ( override.exists() )
        {
            {
                InputStream in = null;
                try
                {
                    in = new BufferedInputStream( new FileInputStream( override ) );
                    overrideProperties.load( in );
                }
                catch ( FileNotFoundException ignore )
                {
                    // ignore
                }
                finally
                {
                    if ( in != null )
                    {
                        in.close();
                    }
                }
            }
            {
                InputStream in = null;
                try
                {
                    in = new BufferedInputStream( new FileInputStream( override ) );
                    combinedProperties.load( in );
                }
                catch ( FileNotFoundException ignore )
                {
                    // ignore
                }
                finally
                {
                    if ( in != null )
                    {
                        in.close();
                    }
                }
            }
        }
        return this;
    }

    public JSWConfig save()
        throws IOException
    {
        PrintWriter out = null;
        try
        {
            if ( !override.getParentFile().exists() && !override.getParentFile().mkdirs() )
            {
                throw new FileNotFoundException( "Could not create file " + override.getAbsolutePath() );
            }
            out = new PrintWriter( new FileWriter( override ) );

            for ( String propertyName : overrideProperties.stringPropertyNames() )
            {
                out.println( propertyName + "=" + overrideProperties.getProperty( propertyName ) );
            }
        }
        finally
        {
            if ( out != null )
            {
                out.close();
            }
        }
        return this;
    }

    public String getProperty( String key )
    {
        return combinedProperties.getProperty( key );
    }

    public JSWConfig setProperty( final String key, final String value )
    {
        overrideProperties.setProperty( key, value );
        combinedProperties.setProperty( key, value );
        return this;
    }

    public JSWConfig addIndexedProperty( final String key, final String value )
    {
        int index = 0;
        for ( String propertyName : combinedProperties.stringPropertyNames() )
        {
            try
            {
                int pos = Integer.valueOf( propertyName.replaceFirst( key + ".", "" ) );
                index = Math.max( pos, index );
            }
            catch ( NumberFormatException ignore )
            {
                // ignore as is not an indexed property
            }
        }
        return setProperty( key + "." + ++index, value );
    }

}
