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

import static junit.framework.Assert.assertTrue;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers.contains;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.sonatype.sisu.jsw.monitor.Launcher;
import org.sonatype.sisu.litmus.testsupport.TestSupport;
import org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers;

/**
 * TODO
 *
 * @since 1.0
 */
public class JSWConfigTest
    extends TestSupport
{

    private JSWConfig jswConfig;

    private File config;

    @Before
    public void setUp()
        throws IOException
    {
        final URL conf = getClass().getResource( "/wrapper.conf" );

        config = testMethod.getTargetDirMethodFile( "configs", "wrapper.conf" );
        config.delete();

        copyFile( new File( conf.getFile() ), config );

        jswConfig = new JSWConfig( config );
        jswConfig.load();
    }

    @Test
    public void propertiesFromConfig()
    {
        assertThat(
            jswConfig.getProperty( "wrapper.java.mainclass" ),
            is( equalTo( "org.tanukisoftware.wrapper.WrapperSimpleApp" ) )
        );
        assertThat(
            jswConfig.getProperty( "wrapper.java.classpath.2" ),
            is( equalTo( "etc/" ) )
        );
    }

    @Test
    public void setProperty()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.mainclass", "foo.Bar" );
        jswConfig.save();
        assertThat(
            config,
            contains( "wrapper.java.mainclass=foo.Bar" )
        );
    }

    @Test
    public void setIndexedProperty()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.classpath.2", "foo/*" );
        jswConfig.save();
        assertThat(
            config,
            contains(
                "wrapper.java.classpath.2=foo/*"
            )
        );
    }

    @Test
    public void addIndexedProperty()
        throws IOException
    {
        jswConfig.addIndexedProperty( "wrapper.java.classpath", "foo/*" );
        jswConfig.addIndexedProperty( "wrapper.java.classpath", "bar/*" );
        jswConfig.save();
        assertThat(
            config,
            contains(
                "wrapper.java.classpath.4=foo/*",
                "wrapper.java.classpath.5=bar/*"
            )
        );
    }

    @Test
    public void addInexistentIndexedProperty()
        throws IOException
    {
        jswConfig.addIndexedProperty( "wrapper.java.additional", "-Dfoo=bar" );
        jswConfig.save();
        assertThat(
            config,
            contains( "wrapper.java.additional.1=-Dfoo=bar" )
        );
    }

    @Test
    public void configureMonitor()
        throws Exception
    {
        jswConfig.configureMonitor( 9001 );
        jswConfig.save();
        assertThat(
            config,
            contains(
                JSWConfig.WRAPPER_JAVA_MAINCLASS + "=" + Launcher.class.getName(),
                JSWConfig.WRAPPER_JAVA_ADDITIONAL + ".3=-D" + Launcher.MONITOR_PORT + "=9001",
                JSWConfig.WRAPPER_JAVA_CLASSPATH + ".4=" + getLauncherFile().getAbsolutePath()
            )
        );
    }

    @Test
    public void configureKeepAlive()
        throws Exception
    {
        jswConfig.configureKeepAlive( 9001 );
        jswConfig.save();
        assertThat(
            config,
            contains(
                JSWConfig.WRAPPER_JAVA_MAINCLASS + "=" + Launcher.class.getName(),
                JSWConfig.WRAPPER_JAVA_ADDITIONAL + ".3=-D" + Launcher.KEEP_ALIVE_PORT + "=9001",
                JSWConfig.WRAPPER_JAVA_CLASSPATH + ".4=" + getLauncherFile().getAbsolutePath()
            )
        );
    }

    @Test
    public void testLauncherPathWithSpaces()
        throws Exception
    {
        URL url = new URL( "file:/path%20with%20spaces/and%20more%20spaces/launcher.jar" );
        File file = jswConfig.urlToFile( url );
        assertTrue(
            file.getAbsolutePath().replace( '\\', '/' ).endsWith( "/path with spaces/and more spaces/launcher.jar" )
        );
    }

    /**
     * Verify that calling write, multiple times, will not append overridden properties multiple times.
     *
     * @throws IOException unexpected
     */
    @Test
    public void subsequentSavesProducesSameFile()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.classpath.2", "foo/*" );
        jswConfig.save();

        // copy written configuration file so we can compare
        final File saved = testMethod.getTargetDirMethodFile( "configs", "wrapper-1.conf" );
        copyFile( config, saved );

        // save again
        jswConfig.save();

        // and check that the last save and previous saved are equal
        assertThat( config, FileMatchers.matchSha1( saved ) );

        // check that original property is written
        assertThat(
            config,
            contains( "wrapper.java.mainclass=org.tanukisoftware.wrapper.WrapperSimpleApp" )
        );
        // check that overridden property is written
        assertThat(
            config,
            contains( "wrapper.java.classpath.2=foo/*" )
        );
    }

    private File getLauncherFile()
        throws URISyntaxException
    {
        URL url = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
        return new File( url.toURI() );
    }
}
