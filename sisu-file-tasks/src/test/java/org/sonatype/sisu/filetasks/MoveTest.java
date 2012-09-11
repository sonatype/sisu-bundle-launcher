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

import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;

import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

/**
 * "Move" task/builder related tests.
 *
 * @since 1.0
 */
public class MoveTest
    extends FileTaskTest
{

    /**
     * Tests moving of an existent file.
     */
    @Test
    public void move01()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().move( path( "dir01/file0101.txt" ) )
                .to( path( "dir02/file0201.txt" ) )
        );
        assertDoesNotExist( "dir01/file0101.txt" );
        assertExists( "dir02/file0201.txt" );
    }

    /**
     * Tests moving an existing directory.
     */
    @Test
    public void move02()
    {
        run(
            builder().copy().directory( file( testClassSourceFile( "set-1" ) ) )
                .to().directory( path( "/" ) ),

            builder().move( path( "dir01" ) )
                .to( path( "dir02" ) )
        );
        assertDoesNotExist( "dir01" );
        assertExists( "dir02" );
        assertExists( "dir02/file0101.txt" );
    }

}
