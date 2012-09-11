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
package org.sonatype.sisu.bl.servlet.jetty.testsuite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.After;
import org.junit.Before;
import org.sonatype.sisu.bl.servlet.jetty.JettyBundle;
import org.sonatype.sisu.bl.servlet.jetty.JettyBundleConfiguration;
import com.google.common.base.Stopwatch;

/**
 * Support class for Jetty based ITs that require a jetty running before the test.
 *
 * @since 1.4
 */
public class JettyRunningITSupport
    extends JettyITSupport
{

    /**
     * Provider used to create Jetty bundles on demand.
     */
    @Inject
    private Provider<JettyBundle> jettyProvider;

    /**
     * Current running Jetty. Lazy created by {@link #jetty()}.
     */
    private JettyBundle jetty;

    @Before
    public void beforeTestIsRunning()
    {
        final Stopwatch stopwatch = startJetty( jetty() );
        testIndex().recordInfo(
            "startup time", stopwatch.elapsedMillis() == 0 ? "already running" : stopwatch.toString()
        );

        assertThat( "Jetty is running before test starts", jetty().isRunning(), is( true ) );
    }

    @After
    public void afterTestWasRunning()
    {
        stopJetty( jetty );
    }

    /**
     * Returns current Jetty. If Jetty was not yet instantiated, Jetty is created and configured.
     *
     * @return current Jetty
     */
    protected final JettyBundle jetty()
    {
        if ( jetty == null )
        {
            jetty = jettyProvider.get();
            final JettyBundleConfiguration config = configureJetty(
                applyDefaultConfiguration( jetty ).getConfiguration()
            );
            if ( config != null )
            {
                jetty.setConfiguration( config );
            }
        }
        return jetty;
    }

    /**
     * Apply default configuration settings to specified Jetty bundle.
     *
     * @param jetty to apply default configurations settings to
     * @return passed in Jetty, for fluent API usage
     */
    public JettyBundle applyDefaultConfiguration( final JettyBundle jetty )
    {
        return jetty;
    }

    /**
     * Template method to be overridden by subclasses that wish to additionally configure Jetty before starting,
     * eventually replacing it.
     *
     * @param configuration Jetty configuration
     * @return configuration that will replace current configuration. If null is returned passed in configuration will
     *         be used
     */
    protected JettyBundleConfiguration configureJetty( JettyBundleConfiguration configuration )
    {
        return configuration;
    }

}