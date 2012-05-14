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

package org.sonatype.sisu.filetasks;

import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

import java.io.File;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;

/**
 * TODO
 *
 * @since 1.0
 */
public class CopyDirectoryTest
    extends FileTaskTest
{

    @Test
    public void copyDirectory01()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
        );
        assertExists( "dir01/file0101.txt" );
        assertExists( "dir01/dir0101/file010101.txt" );
    }

    @Test
    public void copyDirectory02()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
                .include( "**/dir0101/*" )
        );
        assertDoesNotExist( "dir01/file0101.txt" );
        assertExists( "dir01/dir0101/file010101.txt" );
    }

    @Test
    public void copyDirectory03()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
                .exclude( "**/file0101.txt" )
        );
        assertDoesNotExist( "dir01/file0101.txt" );
        assertExists( "dir01/dir0101/file010101.txt" );
    }

    @Test
    public void copyDirectory04()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
                .exclude( "**/*.txt" )
                .includeEmptyDirectories()
        );
        assertExists( "dir01/dir0101" );
        assertDoesNotExist( "dir01/file0101.txt" );
        assertDoesNotExist( "dir01/dir0101/file010101.txt" );
    }

    @Test
    public void copyDirectory05()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
                .exclude( "**/*.txt" )
                .excludeEmptyDirectories()
        );
        assertDoesNotExist( "dir01" );
    }

    @Test
    public void copyDirectory06()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().copy()
                .directory( file( testClassSourceFile( "set-2" ) ) )
                .to().directory( path( "/" ) )
                .overwriteNewer()
        );
        assertSameContent(
            testClassSourceFile( "set-2/dir01/file0101.txt" ),
            testMethodTargetFile( "dir01/file0101.txt" )
        );
    }

    @Test
    public void copyDirectory07()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().copy()
                .directory( file( testClassSourceFile( "set-2" ) ) )
                .to().directory( path( "/" ) )
                .doNotOverwriteNewer()
        );
        assertSameContent(
            testClassSourceFile( "set-1/dir01/file0101.txt" ),
            testMethodTargetFile( "dir01/file0101.txt" )
        );
    }

    @Test
    public void copyDirectory08()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
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
    public void copyDirectory09()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
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
    public void copyDirectory10()
    {
        Properties properties = new Properties();
        properties.setProperty( "property.properties", "properties" );

        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
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
    public void copyDirectory11()
    {
        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
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
    public void copyDirectory12()
    {
        Properties properties = new Properties();
        properties.setProperty( "property.properties", "properties" );

        run(
            builder().copy()
                .directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) )
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
     * Test that copy fails if directory to be copied does not exist.
     */
    @Test( expected = BuildException.class )
    public void copyDirectory13()
    {
        run(
            builder().copy()
                .directory( file( new File( String.valueOf( System.currentTimeMillis() ) ) ) )
                .to().directory( path( "/" ) )
        );
    }

    /**
     * Test that copy does not fail if directory to be copied does not exist and flag is set.
     */
    @Test
    public void copyDirectory14()
    {
        run(
            builder().copy()
                .directory( file( new File( String.valueOf( System.currentTimeMillis() ) ) ) )
                .to().directory( path( "/" ) )
                .doNotFailIfSourceDoesNotExist()
        );
    }

}
