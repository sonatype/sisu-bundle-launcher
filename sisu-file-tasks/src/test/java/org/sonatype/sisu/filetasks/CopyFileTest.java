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
package org.sonatype.sisu.filetasks;

import org.apache.tools.ant.BuildException;
import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;

import java.io.File;
import java.util.Properties;

import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

/**
 * TODO
 *
 * @since 1.0
 */
public class CopyFileTest
    extends FileTaskTest
{

    @Test
    public void copyFile01()
    {
        run(
            builder().copy().file( file( testClassSourceFile( "set-1/dir01/file0101.txt" ) ) )
                .to().directory( path( "/dir01" ) )
        );
        assertExists( "dir01/file0101.txt" );
    }

    @Test
    public void copyFile02()
    {
        run(
            builder().copy().file( file( testClassSourceFile( "set-1/dir01/file0101.txt" ) ) )
                .to().file( path( "/file01.txt" ) )
        );
        assertExists( "file01.txt" );
    }

    @Test
    public void copyFile03()
    {
        run(
            builder().copy().file( file( testClassSourceFile( "set-1/dir01/file0101.txt" ) ) )
                .to().directory( path( "/dir01" ) )
                .filterUsing( System.getProperties() )
        );
        assertContains(
            "dir01/file0101.txt",
            "property.systemProperties=" + System.getProperty( "java.version" ),
            "property.manual=${property.manual}",
            "property.properties=${property.properties}",
            "property.propertiesFile=${property.propertiesFile}"
        );
    }

    @Test
    public void copyFile04()
    {
        run(
            builder().copy().file( file( testClassSourceFile( "set-1/dir01/file0101.txt" ) ) )
                .to().directory( path( "/dir01" ) )
                .filterUsing( "property.manual", "manual" )
        );
        assertContains(
            "dir01/file0101.txt",
            "property.systemProperties=${java.version}",
            "property.manual=manual",
            "property.properties=${property.properties}",
            "property.propertiesFile=${property.propertiesFile}"
        );
    }

    @Test
    public void copyFile05()
    {
        Properties properties = new Properties();
        properties.setProperty( "property.properties", "properties" );

        run(
            builder().copy().file( file( testClassSourceFile( "set-1/dir01/file0101.txt" ) ) )
                .to().directory( path( "/dir01" ) )
                .filterUsing( properties )
        );
        assertContains(
            "dir01/file0101.txt",
            "property.systemProperties=${java.version}",
            "property.manual=${property.manual}",
            "property.properties=properties",
            "property.propertiesFile=${property.propertiesFile}"
        );
    }

    @Test
    public void copyFile06()
    {
        run(
            builder().copy().file( file( testClassSourceFile( "set-1/dir01/file0101.txt" ) ) )
                .to().directory( path( "/dir01" ) )
                .filterUsing( testClassSourceFile( "filter.properties" ) )
        );
        assertContains(
            "dir01/file0101.txt",
            "property.systemProperties=${java.version}",
            "property.manual=${property.manual}",
            "property.properties=${property.properties}",
            "property.propertiesFile=propertiesFile"
        );
    }

    @Test
    public void copyFile07()
    {
        Properties properties = new Properties();
        properties.setProperty( "property.properties", "properties" );

        run(
            builder().copy().file( file( testClassSourceFile( "set-1/dir01/file0101.txt" ) ) )
                .to().directory( path( "/dir01" ) )
                .filterUsing( System.getProperties() )
                .filterUsing( "property.manual", "manual" )
                .filterUsing( properties )
                .filterUsing( testClassSourceFile( "filter.properties" ) )
        );
        assertContains(
            "dir01/file0101.txt",
            "property.systemProperties=" + System.getProperty( "java.version" ),
            "property.manual=manual",
            "property.properties=properties",
            "property.propertiesFile=propertiesFile"
        );
    }

    /**
     * Test that copy fails if file to be copied does not exist.
     */
    @Test( expected = BuildException.class )
    public void copyFile08()
    {
        run(
            builder().copy()
                .file( file( new File( String.valueOf( System.currentTimeMillis() ) ) ) )
                .to().directory( path( "/" ) )
        );
    }

    /**
     * Test that copy does not fail if file to be copied does not exist and flag is set.
     */
    @Test
    public void copyFile09()
    {
        run(
            builder().copy()
                .file( file( new File( String.valueOf( System.currentTimeMillis() ) ) ) )
                .to().directory( path( "/" ) )
                .doNotFailIfSourceDoesNotExist()
        );
    }

}
