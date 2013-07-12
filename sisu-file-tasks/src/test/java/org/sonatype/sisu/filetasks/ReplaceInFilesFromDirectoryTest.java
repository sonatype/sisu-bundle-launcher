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
 * "Replace in files from directory" task/builder related tests.
 *
 * @since 1.3
 */
public class ReplaceInFilesFromDirectoryTest
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
     * Verify that value is replaced in all files.
     */
    @Test
    public void replaceInFilesFromDirectory01()
    {
        run(
            builder().replace().inFilesFromDirectory( path( "/" ) )
                .replace( "should be", "were" )
        );
        {
            final File replaced = testMethodTargetFile( "file01.txt" );
            assertThat( replaced, contains( "Some values in this file were replaced" ) );
        }
        {
            final File replaced = testMethodTargetFile( "file02.txt" );
            assertThat( replaced, contains( "Some values in this file were replaced" ) );
        }
    }

    /**
     * Verify that replace does not fail if directory does not exist.
     */
    @Test
    public void replaceInFilesFromDirectory02()
    {
        run(
            builder().replace().inFilesFromDirectory( path( "to-a-directory-that-does-not-exist" ) )
                .replace( "foo", "bar" )
                .doNotFailIfDirectoryDoesNotExist()
        );
    }

    /**
     * Verify that replace fails if directory does not exist.
     */
    @Test( expected = BuildException.class )
    public void replaceInFilesFromDirectory03()
    {
        run(
            builder().replace().inFilesFromDirectory( path( "to-a-directory-that-does-not-exist" ) )
                .replace( "foo", "bar" )
                .failIfDirectoryDoesNotExist()
        );
    }

    /**
     * Verify that replace do not fail if value to be replaced does not exist.
     */
    @Test
    public void replaceInFilesFromDirectory04()
    {
        run(
            builder().replace().inFilesFromDirectory( path( "/" ) )
                .replace( "something that does not exist", "with whatever" )
                .doNotFailIfNoReplacementWasMade()
        );
    }

    /**
     * Verify that replace fails if value to be replaced does not exist.
     */
    @Test( expected = BuildException.class )
    public void replaceInFilesFromDirectory05()
    {
        run(
            builder().replace().inFilesFromDirectory( path( "/" ) )
                .replace( "something that does not exist", "with whatever" )
                .failIfNoReplacementWasMade()
        );
    }

    /**
     * Verify that value is only replaced in included files.
     */
    @Test
    public void replaceInFilesFromDirectory06()
    {
        run(
            builder().replace().inFilesFromDirectory( path( "/" ) )
                .replace( "should be", "were" )
                .include( "**/file02.txt" )
        );
        {
            final File replaced = testMethodTargetFile( "file01.txt" );
            assertThat( replaced, contains( "Some values in this file should be replaced." ) );
        }
        {
            final File replaced = testMethodTargetFile( "file02.txt" );
            assertThat( replaced, contains( "Some values in this file were replaced" ) );
        }
    }

    /**
     * Verify that value is not replaced in excluded files.
     */
    @Test
    public void replaceInFilesFromDirectory07()
    {
        run(
            builder().replace().inFilesFromDirectory( path( "/" ) )
                .replace( "should be", "were" )
                .exclude( "**/file01.txt" )
        );
        {
            final File replaced = testMethodTargetFile( "file01.txt" );
            assertThat( replaced, contains( "Some values in this file should be replaced." ) );
        }
        {
            final File replaced = testMethodTargetFile( "file02.txt" );
            assertThat( replaced, contains( "Some values in this file were replaced" ) );
        }
    }

}
