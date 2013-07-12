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
package org.sonatype.sisu.filetasks;

import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;

import java.util.Properties;

import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;
import static org.sonatype.sisu.filetasks.support.FileTaskTest.PropertyBinding.property;

/**
 * TODO
 *
 * @since 1.0
 */
public class PropertiesTest
    extends FileTaskTest
{

    @Test
    public void properties01()
    {
        run(
            builder().properties( path( "dir01/file0101.properties" ) )
                .property( "key.1", "value.1.changed" )

        );
        assertContains( "dir01/file0101.properties",
                        property( "key.1", "value.1.changed" ) );
    }

    @Test
    public void properties02()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().properties( path( "dir01/file0101.properties" ) )
                .property( "key.1", "value.1.changed" )
        );
        assertContains( "dir01/file0101.properties",
                        property( "key.1", "value.1.changed" ),
                        property( "key.2", "value.2.original" ) );
    }

    @Test
    public void properties03()
    {
        Properties properties = new Properties();
        properties.setProperty( "key.2", "value.2.changed" );

        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().properties( path( "dir01/file0101.properties" ) )
                .properties( properties )
        );
        assertContains( "dir01/file0101.properties",
                        property( "key.1", "value.1.original" ),
                        property( "key.2", "value.2.changed" ) );
    }

    @Test
    public void properties04()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().properties( path( "dir01/file0101.properties" ) )
                .properties( testClassSourceFile( "filter.properties" ) )
        );
        assertContains( "dir01/file0101.properties",
                        property( "key.1", "value.1.original" ),
                        property( "key.2", "value.2.original" ),
                        property( "key.3", "value.3.changed" ) );
    }

    @Test
    public void properties05()
    {
        Properties properties = new Properties();
        properties.setProperty( "key.2", "value.2.changed" );

        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().properties( path( "dir01/file0101.properties" ) )
                .property( "key.1", "value.1.changed" )
                .properties( properties )
                .properties( testClassSourceFile( "filter.properties" ) )
        );
        assertContains( "dir01/file0101.properties",
                        property( "key.1", "value.1.changed" ),
                        property( "key.2", "value.2.changed" ),
                        property( "key.3", "value.3.changed" ) );
    }

    @Test
    public void properties06()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().properties( path( "dir01/file0101.properties" ) )
                .removeProperty( "key.1" )
        );
        assertDoesNotContains( "dir01/file0101.properties", "key1" );
        assertContains( "dir01/file0101.properties",
                        property( "key.2", "value.2.original" ) );
    }

    @Test
    public void properties07()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().properties( path( "dir01/file0101.properties" ) )
                .removeAllProperties()
        );
        assertDoesNotContains( "dir01/file0101.properties", "key1", "key2" );
    }

}
