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
package org.sonatype.sisu.bl.internal.support;

import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.BundleConfiguration;
import org.sonatype.sisu.goodies.common.ComponentSupport;
import org.sonatype.sisu.goodies.common.Mutex;
import com.google.common.base.Throwables;

/**
 * TODO
 *
 * @since 1.0
 */
public abstract class BundleLifecycle<T extends Bundle, C extends BundleConfiguration>
        extends ComponentSupport
        implements Bundle<T, C>, Lifecycle {

    private final Mutex mutex;

    private final LifecycleHandlerContext state;


    public BundleLifecycle() {
        mutex = new Mutex();
        state = new LifecycleHandlerContext(new Handler());
    }

    @Override
    public T prepare() {
        synchronized (mutex) {

            cleanup();

            state.prepare();
            state.perform();
            state.commit();

            return (T) this;
        }
    }

    @Override
    public T cleanup() {
        synchronized (mutex) {

            state.clean();
            state.perform();
            state.commit();

            return (T) this;
        }
    }

    @Override
    public T start() {
        synchronized (mutex) {

            prepare();

            state.start();
            state.perform();
            state.commit();

            return (T) this;
        }
    }

    @Override
    public T stop() {
        synchronized (mutex) {

            state.stop();
            state.perform();
            state.commit();

            return (T) this;
        }
    }

    private class Handler
            implements LifecycleHandler {

        Throwable failure;

        @Override
        public void log(final String message) {
            BundleLifecycle.this.log.debug(message);
        }

        @Override
        public boolean success() {
            return failure == null;
        }

        @Override
        public void throwFailureIfFailed() {
            if (success()) {
                return;
            }
            throw Throwables.propagate( failure );
        }

        @Override
        public void doPrepare() {
            try {
                failure = null;
                BundleLifecycle.this.doPrepare();
            } catch (Throwable e) {
                failure = e;
            }
        }

        @Override
        public void doClean() {
            try {
                failure = null;
                BundleLifecycle.this.doClean();
            } catch (Throwable e) {
                failure = e;
            }
        }

        @Override
        public void doStart() {
            try {
                failure = null;
                BundleLifecycle.this.doStart();
            } catch (Throwable e) {
                failure = e;
            }
        }

        @Override
        public void doStop() {
            try {
                failure = null;
                BundleLifecycle.this.doStop();
            } catch (Throwable e) {
                failure = e;
            }
        }
    }

}