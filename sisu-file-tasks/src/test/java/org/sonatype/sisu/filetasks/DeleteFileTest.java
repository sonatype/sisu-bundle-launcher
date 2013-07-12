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

import org.apache.tools.ant.BuildException;
import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;

import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

/**
 * "Delete File" task/builder related tests.
 *
 * @since 1.0
 */
public class DeleteFileTest
    extends FileTaskTest
{

    /**
     * Test that file is removed.
     */
    @Test
    public void deleteFile01()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().delete().file( path( "dir01/file0101.txt" ) )
        );
        assertDoesNotExist( "dir01/file0101.txt" );
    }

    /**
     * Test that delete does not fail if file to be deleted does not exist.
     *
     * @since 1.0
     */
    @Test
    public void deleteFile02()
    {
        run(
            builder().delete().file( path( "dir01/file0101.txt" ) ).doNotFailIfNotPresent()
        );
        assertDoesNotExist( "set-1/dir01/file0101.txt" );
    }

    /**
     * Test that delete fails if file to be deleted does not exist.
     *
     * @since 1.0
     */
    @Test( expected = BuildException.class )
    public void deleteFile03()
    {
        run(
            builder().delete().file( path( "dir01/file0101.txt" ) ).failIfNotPresent()
        );
    }

}
