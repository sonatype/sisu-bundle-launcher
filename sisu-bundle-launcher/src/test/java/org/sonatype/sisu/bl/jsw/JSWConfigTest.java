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
package org.sonatype.sisu.bl.jsw;

import static junit.framework.Assert.assertTrue;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers.contains;
import static org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers.doesNotContain;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sonatype.sisu.litmus.testsupport.TestSupport;
import org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers;
import org.sonatype.sisu.litmus.testsupport.junit.TestIndexRule;

/**
 * Tests for {@link JSWConfig}
 *
 * @since 1.0
 */
public class JSWConfigTest
    extends TestSupport
{

    @Rule
    public TestIndexRule testIndex = new TestIndexRule( util.getTargetDir() );

    private JSWConfig jswConfig;

    private File config;

    @Before
    public void setUp()
        throws IOException
    {
        final URL conf = getClass().getResource( "/wrapper.conf" );

        config = new File( testIndex.getDirectory( "configs" ), "wrapper.conf" );

        config.delete();

        copyFile( new File( conf.getFile() ), config );

        jswConfig = new JSWConfig( config );
        jswConfig.load();
    }

    @Test
    public void propertiesFromConfig()
    {
        assertThat(
            jswConfig.getProperty( "wrapper.java.mainclass" ),
            is( equalTo( "org.tanukisoftware.wrapper.WrapperSimpleApp" ) )
        );
        assertThat(
            jswConfig.getProperty( "wrapper.java.classpath.2" ),
            is( equalTo( "etc/" ) )
        );
    }

    @Test
    public void setProperty()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.mainclass", "foo.Bar" );
        jswConfig.save();
        assertThat(
            config,
            contains( "#  explicit overrides" )
        );
        assertThat(
              config,
              contains( "wrapper.java.mainclass=foo.Bar" )
          );
    }

    @Test
    public void setIndexedProperty()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.classpath.2", "foo/*" );
        jswConfig.save();
        assertThat(
            config,
            contains(
                "wrapper.java.classpath.2=foo/*"
            )
        );
    }

    @Test
    public void addIndexedProperty()
        throws IOException
    {
        jswConfig.addIndexedProperty( "wrapper.java.classpath", "foo/*" );
        jswConfig.addIndexedProperty( "wrapper.java.classpath", "bar/*" );
        jswConfig.save();
        assertThat(
            config,
            contains(
                "wrapper.java.classpath.4=foo/*",
                "wrapper.java.classpath.5=bar/*"
            )
        );
    }

    @Test
    public void addInexistentIndexedProperty()
        throws IOException
    {
        jswConfig.addIndexedProperty( "wrapper.java.additional", "-Dfoo=bar" );
        jswConfig.save();
        assertThat(
            config,
            contains( "wrapper.java.additional.1=-Dfoo=bar" )
        );
    }

    @Test
    public void testLauncherPathWithSpaces()
        throws Exception
    {
        URL url = new URL( "file:/path%20with%20spaces/and%20more%20spaces/launcher.jar" );
        File file = jswConfig.urlToFile( url );
        assertTrue(
            file.getAbsolutePath().replace( '\\', '/' ).endsWith( "/path with spaces/and more spaces/launcher.jar" )
        );
    }

    /**
     * Verify that calling write, multiple times, will not append overridden properties multiple times.
     *
     * @throws IOException unexpected
     */
    @Test
    public void subsequentSavesProducesSameFile()
        throws IOException
    {
        jswConfig.setProperty( "wrapper.java.classpath.2", "foo/*" );
        jswConfig.save();

        // copy written configuration file so we can compare
        final File saved = new File( testIndex.getDirectory( "configs" ), "wrapper-1.conf" );
        copyFile( config, saved );

        // save again
        jswConfig.save();

        // and check that the last save and previous saved are equal
        assertThat( config, FileMatchers.matchSha1( saved ) );

        // check that original property is written
        assertThat(
            config,
            contains( "wrapper.java.mainclass=org.tanukisoftware.wrapper.WrapperSimpleApp" )
        );
        // check that overridden property is written
        assertThat(
            config,
            contains( "wrapper.java.classpath.2=foo/*" )
        );
    }


    @Test
    public void setJavaCommand() throws Exception {
      jswConfig.setJavaCommand("/foo/bis/baz/java");
      jswConfig.save();

      assertThat(
          config,
          contains("wrapper.java.command=/foo/bis/baz/java")
      );
    }

    @Test
    public void wrapperCommentsPerTypeOfOverride() throws Exception {

        // backup system properties
        Properties backup = new Properties();
        backup.putAll(System.getProperties());

        // setup sys props
        Map<String,String> testProps = Maps.newHashMap();
        testProps.put("wrapper.system.prop", "bar");
        System.getProperties().putAll(testProps);

        jswConfig.load(); // loads system properties

        // set an explicit property as well
        jswConfig.setProperty("wrapper.explicit.prop", "baz");

        // save again to write out config with system properties
        jswConfig.save();

        // check that overridden properties are written
        assertThat(
            config,
            contains("#  system overrides")
        );

        assertThat(
            config,
            contains("#  explicit overrides")
        );

        assertThat(
            config,
            contains("wrapper.explicit.prop=baz")
        );

        assertThat(
            config,
            contains("wrapper.system.prop=bar")
        );

        // reset system properties
        System.setProperties(backup);

    }

  @Test
  public void wrapperSystemPropertiesOverride() throws Exception {
      Properties backup = new Properties();
      backup.putAll(System.getProperties());

      // test setup
      Map<String,String> testProps = Maps.newHashMap();
      testProps.put(JSWConfig.WRAPPER_JAVA_COMMAND,"/my/custom/java");
      testProps.put("nonwrapper.prop","special");
      testProps.put("wrapper.prop","something");
     System.getProperties().putAll(testProps);

      // load and then save capture system properties
      jswConfig.load();
      jswConfig.save();

      // check that overridden properties are written
      assertThat(
          config,
          contains("#  system overrides")
      );

      assertThat(
          config,
          contains("wrapper.java.command=/my/custom/java")
      );

      assertThat(
          config,
          contains("wrapper.prop=something")
      );

      assertThat(
          config,
          doesNotContain("nonwrapper.prop=special")
      );

      // reset system properties
      System.setProperties(backup);

  }

  @Test
  public void wrapperSystemPropertiesOverrideDisabled() throws Exception {
    Properties backup = new Properties();
    backup.putAll(System.getProperties());

    Map<String,String> testProps = Maps.newHashMap();
    testProps.put("wrapper.prop","something");
    System.getProperties().putAll(testProps);

    jswConfig.setWrapperSystemOverridesEnabled(false);
    jswConfig.load();

    jswConfig.save();

    // check that overridden properties are written
    assertThat(
        config,
        doesNotContain("#  system overrides")
    );
    assertThat(
        config,
        doesNotContain("wrapper.prop=something")
    );

    // reset system properties
    System.setProperties(backup);

  }
}
