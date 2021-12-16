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

package org.sonatype.sisu.bl.internal.support;

import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.BundleConfiguration;
import org.sonatype.sisu.bl.BundleStatistics;
import org.sonatype.sisu.goodies.testsupport.TestSupport;

import org.junit.Test;

/**
 * Tests for {#link BundleLifecycle}.
 *
 * @since 1.0
 */
public class BundleLifecycleTest
    extends TestSupport
{

  @Test
  public void stop() {
    new TestBundleLifecycle().stop();
  }

  @Test
  public void stop_stop() {
    new TestBundleLifecycle().stop().stop();
  }

  @Test
  public void prepare() {
    new TestBundleLifecycle().prepare();
  }

  @Test(expected = RuntimeException.class)
  public void prepareWithError() {
    new TestBundleLifecycle()
    {
      @Override
      public void doPrepare() {
        throw new RuntimeException("ops");
      }
    }.prepare();
  }

  @Test
  public void prepare_start() {
    new TestBundleLifecycle().prepare().start();
  }

  @Test
  public void prepare_prepare() {
    new TestBundleLifecycle().prepare().prepare();
  }

  @Test
  public void start() {
    new TestBundleLifecycle().start();
  }

  @Test
  public void start_start() {
    TestBundleLifecycle bundle = new TestBundleLifecycle();
    bundle.start();
    bundle.start();
  }

  @Test
  public void start_stop() {
    new TestBundleLifecycle().start().stop();
  }

  private static class TestBundleLifecycle
      extends BundleLifecycle
  {

    @Override
    public void doPrepare() {
      // Do nothing
    }

    @Override
    public void doClean() {
      // Do nothing
    }

    @Override
    public void doStart() {
      // Do nothing
    }

    @Override
    public void doStop() {
      // Do nothing
    }

    @Override
    public BundleConfiguration getConfiguration() {
      return null;
    }

    @Override
    public Bundle setConfiguration(final BundleConfiguration configuration) {
      return null;
    }

    @Override
    public boolean isRunning() {
      return false;
    }

    @Override
    public BundleStatistics statistics() {
      return null;
    }

  }


}
