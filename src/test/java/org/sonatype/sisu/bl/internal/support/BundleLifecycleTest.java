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
package org.sonatype.sisu.bl.internal.support;

import org.junit.Test;
import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.BundleConfiguration;
import org.sonatype.sisu.litmus.testsupport.TestSupport;

/**
 * TODO
 *
 * @since 1.0
 */
public class BundleLifecycleTest
        extends TestSupport {

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
        new TestBundleLifecycle() {
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
            extends BundleLifecycle {

        @Override
        public void doPrepare() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void doClean() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void doStart() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void doStop() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public BundleConfiguration getConfiguration() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Bundle setConfiguration(final BundleConfiguration configuration) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public State getState() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}