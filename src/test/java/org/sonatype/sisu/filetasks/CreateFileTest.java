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

import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;
import org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;


import static org.hamcrest.MatcherAssert.*;

/**
 * Tests creating files.
 *
 */
public class CreateFileTest extends FileTaskTest{


    @Test
    public void createFileContainingContentWithDefaultEncoding()
    {
        run(builder().create().file( file( testMethodTargetFile( "test.txt" ) ) ).containing("my special content"));
        assertExists( "test.txt" );
        assertThat(testMethodTargetFile("test.txt"), FileMatchers.containsOnly("my special content"));

    }

    @Test
    public void createFileWithNoContent()
    {
        run(builder().create().file( file( testMethodTargetFile( "test.txt" ) ) ));
        assertExists( "test.txt" );
        assertThat(testMethodTargetFile("test.txt"), FileMatchers.sized(0L));
    }

    @Test
    public void createFileWithEncoding()
    {
        run(builder().create().file( file( testMethodTargetFile( "test.txt" ) ) ).containing("@@@").encodedAs("UTF-8"));
        assertExists( "test.txt" );
        assertThat(testMethodTargetFile("test.txt"), FileMatchers.containsOnly("@@@"));
    }

    @Test
    public void createFileWithEmptyContent()
    {
        run(builder().create().file( file( testMethodTargetFile( "test.txt" ) ) ).containing(""));
        assertExists( "test.txt" );
        assertThat(testMethodTargetFile("test.txt"), FileMatchers.sized(0L));
    }


    @Test
    public void createFileOnDirectoryWithRelativePath()
    {
        run(builder().create().file( path( "relative.txt" ) ).containing("relative path file"));
        assertExists( "relative.txt" );
        assertThat(testMethodTargetFile("relative.txt"), FileMatchers.containsOnly("relative path file"));
    }



}
