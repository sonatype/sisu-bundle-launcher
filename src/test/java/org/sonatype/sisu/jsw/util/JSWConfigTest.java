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

package org.sonatype.sisu.jsw.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.sonatype.sisu.litmus.testsupport.TestSupport;
import org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers;

/**
 * TODO
 *
 * @since 1.0
 */
public class JSWConfigTest
    extends TestSupport
{

    private JSWConfig jswConfig;

    private File additionalConfig;

    @Before
    public void setUp()
        throws IOException
    {
        URL conf = getClass().getResource( "/wrapper.conf" );

        additionalConfig = testMethod.getTargetDirMethodFile( "configs", "wrapper-additional.conf" );
        additionalConfig.delete();

        jswConfig = new JSWConfig( new File( conf.getFile() ), additionalConfig );
        jswConfig.load();
    }

    @Test
    public void propertiesFromConfig()
    {
        assertThat( jswConfig.getProperty( "wrapper.java.mainclass" ),
                    is( equalTo( "org.tanukisoftware.wrapper.WrapperSimpleApp" ) ) );
        assertThat( jswConfig.getProperty( "wrapper.java.classpath.2" ),
                    is( equalTo( "etc/" ) ) );
    }

    @Test
    public void setProperty()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.mainclass", "foo.Bar" );
        jswConfig.save();
        assertThat( additionalConfig, FileMatchers.contains( "wrapper.java.mainclass=foo.Bar" ) );
    }

    @Test
    public void setIndexedProperty()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.classpath.2", "foo/*" );
        jswConfig.save();
        assertThat( additionalConfig, FileMatchers.contains( "wrapper.java.classpath.2=foo/*" ) );
    }

    @Test
    public void addIndexedProperty()
        throws IOException
    {
        jswConfig.addIndexedProperty( "wrapper.java.classpath", "foo/*" );
        jswConfig.addIndexedProperty( "wrapper.java.classpath", "bar/*" );
        jswConfig.save();
        assertThat( additionalConfig, FileMatchers.contains( "wrapper.java.classpath.4=foo/*",
                                                             "wrapper.java.classpath.5=bar/*") );
    }

    @Test
    public void addInexistentIndexedProperty()
        throws IOException
    {
        jswConfig.addIndexedProperty( "wrapper.java.additional", "-Dfoo=bar" );
        jswConfig.save();
        assertThat( additionalConfig, FileMatchers.contains( "wrapper.java.additional.1=-Dfoo=bar" ) );
    }

}
