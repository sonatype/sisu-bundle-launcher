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

import org.apache.tools.ant.BuildException;
import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;

import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

/**
 * "Delete Directory" task/builder related tests.
 *
 * @since 1.0
 */
public class DeleteDirectoryTest
    extends FileTaskTest
{

    /**
     * Test that directory is deleted, including directories that becomes empty.
     */
    @Test
    public void deleteDirectory01()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/set-1" ) ),

            builder().delete().directory( path( "set-1" ) )
        );
        assertDoesNotExist( "set-1" );
        assertDoesNotExist( "dir01" );
        assertDoesNotExist( "dir01/file0101.txt" );
    }

    /**
     * Test that directories that become empty will not be deleted.
     */
    @Test
    public void deleteDirectory02()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/set-1" ) ),

            builder().delete().directory( path( "set-1" ) )
                .excludeEmptyDirectories()
        );
        assertExists( "set-1" );
        assertDoesNotExist( "dir01" );
        assertDoesNotExist( "dir01/file0101.txt" );
    }

    /**
     * Test that delete does not fail if directory to be deleted does not exist.
     *
     * @since 1.0
     */
    @Test
    public void deleteDirectory03()
    {
        run(
            builder().delete().directory( path( "set-1" ) ).doNotFailIfNotPresent()
        );
        assertDoesNotExist( "set-1" );
    }

    /**
     * Test that delete fails if directory to be deleted does not exist.
     *
     * @since 1.0
     */
    @Test( expected = BuildException.class )
    public void deleteDirectory04()
    {
        run(
            builder().delete().directory( path( "set-1" ) ).failIfNotPresent()
        );
    }

}
