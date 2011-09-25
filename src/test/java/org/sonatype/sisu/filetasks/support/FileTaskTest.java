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

package org.sonatype.sisu.filetasks.support;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.slf4j.Logger;
import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.task.internal.PropertiesHelper;
import org.sonatype.sisu.litmus.testsupport.inject.InjectedTestSupport;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.sonatype.sisu.filetasks.FileTaskRunner.onDirectory;

/**
 * TODO
 */
public abstract class FileTaskTest
    extends InjectedTestSupport
{

    @Inject
    private Logger logger;

    @Inject
    private FileTaskBuilder builder;

    @Inject
    @Named( "${project.build.directory}" )
    private File targetDirectory;

    private File testClassSourceFile;

    private File testMethodSourceFile;

    private File testMethodTargetFile;

    @Before
    public void before()
    {

        assertThat( logger, notNullValue() );

        testClassSourceFile =
            new File(
                new File(
                    new File( targetDirectory, "test-classes" ),
                    this.getClass().getPackage().getName().replace( ".", "/" )
                ),
                this.getClass().getSimpleName()
            );

        testMethodSourceFile = new File( testClassSourceFile, testName.getMethodName() );

        testMethodTargetFile =
            new File(
                new File(
                    new File(
                        new File( targetDirectory, "overlays" ),
                        this.getClass().getPackage().getName().replace( ".", "/" )
                    ),
                    this.getClass().getSimpleName()
                ),
                testName.getMethodName()
            );

        try
        {
            FileUtils.deleteDirectory( testMethodTargetFile );
        }
        catch ( IOException ignore )
        {
            // ignore
        }
        assertThat( "File " + testMethodTargetFile.getAbsolutePath() + " does not exist",
                    testMethodTargetFile.exists(),
                    is( false ) );
    }

    protected File testClassSourceFile( String subPath )
    {
        return new File( testClassSourceFile, subPath );
    }

    protected File testMethodSourceFile( String subPath )
    {
        return new File( testMethodSourceFile, subPath );
    }

    protected File testMethodTargetFile( String subPath )
    {
        return new File( testMethodTargetFile, subPath );
    }

    protected void assertExists( String path )
    {
        assertThat( "File " + path + " exists", new File( testMethodTargetFile, path ).exists(), is( true ) );
    }

    protected void assertDoesNotExist( String path )
    {
        assertThat( "File " + path + " does not exist", new File( testMethodTargetFile, path ).exists(), is( false ) );
    }

    protected void assertSameContent( File file1, File file2 )
    {
        InputStream in1 = null;
        InputStream in2 = null;

        try
        {
            in1 = new BufferedInputStream( new FileInputStream( file1 ) );
            in2 = new BufferedInputStream( new FileInputStream( file2 ) );

            assertThat(
                "File " + file1.getAbsolutePath() + " has same content as " + file2.getAbsolutePath(),
                IOUtil.contentEquals( in1, in2 ), Matchers.is( true )
            );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( in1 );
            IOUtil.close( in2 );
        }
    }

    protected void assertContains( String path, String... values )
    {
        assertExists( path );
        File file = testMethodTargetFile( path );

        InputStream in = null;

        try
        {
            in = new BufferedInputStream( new FileInputStream( file ) );
            String content = IOUtil.toString( in );
            for ( String value : values )
            {
                assertThat(
                    "File " + file.getAbsolutePath(),
                    content,
                    containsString( value )
                );
            }

        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( in );
        }
    }

    protected void assertContains( String path, PropertyBinding... bindings )
    {

        assertExists( path );
        File file = testMethodTargetFile( path );

        Properties properties = PropertiesHelper.load( file );
        for ( PropertyBinding binding : bindings )
        {
            assertThat(
                "Property '" + binding.key + " in file " + file.getAbsolutePath(),
                properties.getProperty( binding.key ),
                is( equalTo( binding.value ) )
            );
        }
    }

    protected void assertDoesNotContains( String path, String... keys )
    {

        assertExists( path );
        File file = testMethodTargetFile( path );

        Properties properties = PropertiesHelper.load( file );
        for ( String key : keys )
        {
            assertThat(
                "Property '" + key + " in file " + file.getAbsolutePath(),
                properties.getProperty( key ),
                is( nullValue() )
            );
        }
    }

    protected void run( FileTask... tasks )
    {
        onDirectory( testMethodTargetFile ).apply( tasks );
    }

    protected FileTaskBuilder builder()
    {
        return builder;
    }

    public static class PropertyBinding
    {

        String key, value;

        PropertyBinding( String key, String value )
        {
            this.key = key;
            this.value = value;
        }

        public static PropertyBinding property( String key, String value )
        {
            return new PropertyBinding( key, value );
        }
    }

}
