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
import org.sonatype.sisu.goodies.common.ComponentSupport;
import org.sonatype.sisu.goodies.common.Mutex;
import org.sonatype.sisu.goodies.common.Time;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.sun.corba.se.spi.monitoring.StatisticsAccumulator;

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

    protected Time cleanupTime;

    protected Time preparationTime;

    protected Time startupTime;

    protected Time stoppingTime;

    public BundleLifecycle() {
        mutex = new Mutex();
        state = new LifecycleHandlerContext(new Handler());
        cleanupTime = Time.millis( 0 );
        preparationTime = Time.millis( 0 );
        startupTime = Time.millis( 0 );
        stoppingTime = Time.millis( 0 );
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
            final Stopwatch stopwatch = new Stopwatch().start();
            try {
                failure = null;
                BundleLifecycle.this.doPrepare();
            } catch (Throwable e) {
                failure = e;
            }
            stopwatch.stop();
            preparationTime = Time.millis( stopwatch.elapsedMillis() );
            startupTime = Time.millis( 0 );
            stoppingTime = Time.millis( 0 );
        }

        @Override
        public void doClean() {
            final Stopwatch stopwatch = new Stopwatch().start();
            try {
                failure = null;
                BundleLifecycle.this.doClean();
            } catch (Throwable e) {
                failure = e;
            }
            stopwatch.stop();
            cleanupTime = Time.millis( stopwatch.elapsedMillis() );
            preparationTime = Time.millis( 0 );
            startupTime = Time.millis( 0 );
            stoppingTime = Time.millis( 0 );
        }

        @Override
        public void doStart() {
            final Stopwatch stopwatch = new Stopwatch().start();
            try {
                failure = null;
                BundleLifecycle.this.doStart();
            } catch (Throwable e) {
                failure = e;
            }
            stopwatch.stop();
            startupTime = Time.millis( stopwatch.elapsedMillis() );
            stoppingTime = Time.millis( 0 );
        }

        @Override
        public void doStop() {
            final Stopwatch stopwatch = new Stopwatch().start();
            try {
                failure = null;
                BundleLifecycle.this.doStop();
            } catch (Throwable e) {
                failure = e;
            }
            stopwatch.stop();
            stoppingTime = Time.millis( stopwatch.elapsedMillis() );
        }
    }

}