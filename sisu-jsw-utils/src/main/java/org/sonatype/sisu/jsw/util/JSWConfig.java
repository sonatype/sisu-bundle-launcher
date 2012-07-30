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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.sonatype.sisu.jsw.monitor.Launcher;

/**
 * JSW configurations file (wrapper.conf) reader/writer.
 *
 * @since 1.0
 */
public class JSWConfig
{

    static final String WRAPPER_JAVA_MAINCLASS = "wrapper.java.mainclass";

    static final String WRAPPER_JAVA_ADDITIONAL = "wrapper.java.additional";

    static final String WRAPPER_JAVA_CLASSPATH = "wrapper.java.classpath";

    /**
     * JSW configuration file.
     * Never null.
     */
    private File config;

    /**
     * Properties read from JSW configuration file.
     * Never null.
     */
    private Properties configProperties;

    /**
     * Properties that are added/overrides.
     * Never null.
     */
    private Properties overrideProperties;

    /**
     * Combined config/override properties where override ones takes precedence.
     * Never null.
     */
    private Properties combinedProperties;

    /**
     * Comment that is written out before the override properties are written.
     * Never null.
     */
    private String overrideComment = "";

    /**
     * Constructor. It will use a default comment.
     *
     * @param config JSW configuration file to be read/written
     */
    public JSWConfig( final File config )
    {
        this( config, null );
    }

    /**
     * Constructor.
     *
     * @param config          JSW configuration file to be read/written
     * @param overrideComment comment that is written out before the override properties are written
     */
    public JSWConfig( final File config, final String overrideComment )
    {
        this.config = config;
        if ( overrideComment != null )
        {
            this.overrideComment = overrideComment;
        }
        else
        {
            this.overrideComment =
                "The following properties are added by sisu-jsw-utils as an override of properties already configured";
        }
        configProperties = new Properties();
        overrideProperties = new Properties();
        combinedProperties = new Properties( configProperties );
    }

    /**
     * Reads JSW configuration file.
     *
     * @return itself, for fluent API usage
     * @throws IOException in case of reading configuration file fails
     */
    public JSWConfig load()
        throws IOException
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
        return this;
    }

    /**
     * Saves original + overridden JSW configuration to file provided in constructor.
     *
     * @return itself, for fluent API usage
     * @throws IOException in case of writing configuration file fails
     */
    public JSWConfig save()
        throws IOException
    {
        PrintWriter out = null;
        try
        {
            out = new PrintWriter( new FileWriter( config, true ) );

            out.println();
            out.println( "# " + overrideComment );

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

    /**
     * Returns a JSW configuration property by its key.
     *
     * @param key key of property
     * @return value of property or null if no such variable exists
     */
    public String getProperty( String key )
    {
        return combinedProperties.getProperty( key );
    }

    /**
     * Sets value of a JSW configuration property that will be written out as an override property.
     *
     * @param key   key of property
     * @param value value of property
     * @return itself, for fluent API usage
     */
    public JSWConfig setProperty( final String key, final String value )
    {
        overrideProperties.setProperty( key, value );
        combinedProperties.setProperty( key, value );
        return this;
    }

    /**
     * Adds an indexed JSW configuration property that will be written out as an override property.
     *
     * @param key   key of property
     * @param value value of property
     * @return itself, for fluent API usage
     */
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

    /**
     * Configures JSW monitor that permits sending commands to running JSW process.
     *
     * @param port monitor port
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    public JSWConfig configureMonitor( int port )
    {
        eventuallyConfigureLoader();

        addIndexedProperty( WRAPPER_JAVA_ADDITIONAL, "-D" + Launcher.MONITOR_PORT + "=" + port );

        return this;
    }

    /**
     * Configures keep alive thread that pings a configured port to check if the running JSW process should stop itself.
     *
     * @param port keep alive port
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    public JSWConfig configureKeepAlive( int port )
    {
        eventuallyConfigureLoader();

        addIndexedProperty( WRAPPER_JAVA_ADDITIONAL, "-D" + Launcher.KEEP_ALIVE_PORT + "=" + port );

        return this;
    }

    /**
     * Configures monitored booter if not already configured
     *
     * @since 1.0
     */
    private void eventuallyConfigureLoader()
    {
        String mainClass = getProperty( WRAPPER_JAVA_MAINCLASS );
        if ( Launcher.class.getName().equals( mainClass ) )
        {
            return;
        }
        setProperty( WRAPPER_JAVA_MAINCLASS, Launcher.class.getName() );
        addIndexedProperty( WRAPPER_JAVA_ADDITIONAL, "-D" + Launcher.LAUNCHER + "=" + mainClass );
        addIndexedProperty( WRAPPER_JAVA_ADDITIONAL, "-D" + Launcher.LOG_TO_SYSTEM_OUT + "=true" );

        try
        {
            URL location = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
            addIndexedProperty( WRAPPER_JAVA_CLASSPATH, urlToFile( location ).getAbsolutePath() );
        }
        catch ( URISyntaxException e )
        {
            throw new IllegalStateException( e.getMessage(), e );
        }
    }

    File urlToFile( URL url )
        throws URISyntaxException
    {
        URI uri = url.toURI();
        return new File( uri );
    }
}
