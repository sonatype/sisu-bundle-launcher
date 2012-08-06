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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;
import static org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers.contains;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.junit.Before;
import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;

/**
 * "Replace in file" task/builder related tests.
 *
 * @since 1.3
 */
public class ReplaceInFileTest
    extends FileTaskTest
{

    @Before
    public void prepareFiles()
    {
        FileTaskRunner.onDirectory( testMethodTargetFile( "/" ) ).apply(
            builder().copy().directory( file( testClassSourceFile( "/" ) ) )
                .to().directory( path( "/" ) )
        );
    }

    /**
     * Verify that value is replaced.
     */
    @Test
    public void replaceInFile01()
    {
        run(
            builder().replace().inFile( path( "file01.txt" ) )
                .replace( "should be", "were" )
        );
        final File replaced = testMethodTargetFile( "file01.txt" );
        assertThat( replaced, contains( "Some values in this file were replaced" ) );
    }

    /**
     * Verify that replace does not fail if file does not exist.
     */
    @Test
    public void replaceInFile02()
    {
        run(
            builder().replace().inFile( path( "to-a-file-that-does-not-exist" ) )
                .replace( "foo", "bar" )
                .doNotFailIfFileDoesNotExist()
        );
    }

    /**
     * Verify that replace fails if file does not exist.
     */
    @Test( expected = BuildException.class )
    public void replaceInFile03()
    {
        run(
            builder().replace().inFile( path( "to-a-file-that-does-not-exist" ) )
                .replace( "foo", "bar" )
                .failIfFileDoesNotExist()
        );
    }

    /**
     * Verify that replace do not fail if value to be replaced does not exist.
     */
    @Test
    public void replaceInFile04()
    {
        run(
            builder().replace().inFile( path( "file01.txt" ) )
                .replace( "something that does not exist", "with whatever" )
                .doNotFailIfNoReplacementWasMade()
        );
    }

    /**
     * Verify that replace fails if value to be replaced does not exist.
     */
    @Test( expected = BuildException.class )
    public void replaceInFile05()
    {
        run(
            builder().replace().inFile( path( "file01.txt" ) )
                .replace( "something that does not exist", "with whatever" )
                .failIfNoReplacementWasMade()
        );
    }

}
