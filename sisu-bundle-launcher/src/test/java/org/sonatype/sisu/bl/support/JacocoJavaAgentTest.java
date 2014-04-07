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
package org.sonatype.sisu.bl.support;

import java.io.File;

import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.BundleConfiguration;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JacocoJavaAgentTest
{
  private Bundle bundle;

  private JacocoJavaAgent agent;

  private File bundleTargetDir;

  @Before
  public void setUp() {
    bundle = mock(Bundle.class);
    BundleConfiguration config = mock(BundleConfiguration.class);

    agent = new JacocoJavaAgent();

    bundleTargetDir = new File(System.getProperty("java.io.tmpdir"));

    when(bundle.getConfiguration()).thenReturn(config);
    when(config.getTargetDirectory()).thenReturn(bundleTargetDir);
  }

  @Test
  public void useDefaultJacocoOutputLocation() {
    String expectedOutputFile = new File(bundleTargetDir, "jacoco.exec").getAbsolutePath();
    assertThat("Jacoco output fle", agent.determineJacocoOutputFile(bundle), equalTo(expectedOutputFile));
  }

  @Test
  public void overrideJacocoOutputLocation() {
    String outputFile = "overridden-jacoco.exec";
    agent.setOutputFile(outputFile);

    String expectedOutputFile = new File(outputFile).getAbsolutePath();

    assertThat("Jacoco output file", agent.determineJacocoOutputFile(bundle), equalTo(expectedOutputFile));
  }
}

