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

package org.sonatype.sisu.bl.testsupport;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import org.sonatype.sisu.bl.DropwizardBundle;
import org.sonatype.sisu.bl.DropwizardBundleConfiguration;
import org.sonatype.sisu.bl.testsupport.StartAndStopStrategy.Strategy;

import com.google.common.base.Stopwatch;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Support class for ITs that require a Dropwizard bundle running before the test.
 *
 * @since 1.8
 */
public class DropwizardRunningITSupport
    extends DropwizardITSupport
{

  /**
   * Provider used to create bundles on demand.
   */
  @Inject
  private Provider<DropwizardBundle> bundleProvider;

  /**
   * Current running bundle. Lazy created by {@link #bundle()}.
   */
  private DropwizardBundle runningBundle;

  private static DropwizardBundle staticBundle;

  @Before
  public void beforeTestIsRunning() {
    final Stopwatch stopwatch = startBundle(bundle());
    testIndex().recordInfo(
        "startup time", stopwatch.elapsed(TimeUnit.MILLISECONDS) == 0 ? "already running" : stopwatch.toString()
    );

    assertThat("Dropwizard bundle was not in running state", bundle().isRunning(), is(true));
  }

  @After
  public void afterTestWasRunning() {
    recordLogs(runningBundle);

    final Strategy strategy = getStartAndStopStrategy();
    if (Strategy.EACH_METHOD.equals(strategy)) {
      stopBundle(runningBundle);
      runningBundle = null;
      staticBundle = null;
    }
    else {
      staticBundle = runningBundle;
    }
  }

  @AfterClass
  public static void afterAllTestsWereRun() {
    stopBundle(staticBundle);
    staticBundle = null;
  }

  /**
   * Returns current running Dropwizard bundle. If bundle was not yet instantiated, a Dropwizard bundle will be created
   * and configured.
   *
   * @return current running Dropwizard bundle
   */
  protected final DropwizardBundle bundle() {
    if (runningBundle == null) {
      if (staticBundle == null) {
        runningBundle = bundleProvider.get();
        final DropwizardBundleConfiguration config = configureBundle(
            applyDefaultConfiguration(runningBundle).getConfiguration()
        );
        if (config != null) {
          runningBundle.setConfiguration(config);
        }
      }
      else {
        runningBundle = staticBundle;
      }
    }
    return runningBundle;
  }

  /**
   * Apply default configuration settings to specified Dropwizard bundle.
   *
   * @param bundle to apply default configurations settings to
   * @return passed in bundle, for fluent API usage
   */
  public DropwizardBundle applyDefaultConfiguration(final DropwizardBundle bundle) {
    bundle.getConfiguration().setYaml(testData().resolveFile("config.yaml"));
    return bundle;
  }

  /**
   * Template method to be overridden by subclasses that wish to additionally configure Dropwizard bundle before
   * starting, eventually replacing it.
   *
   * @param configuration Dropwizard bundle configuration
   * @return configuration that will replace current configuration. If null is returned passed in configuration will
   *         be used
   */
  protected DropwizardBundleConfiguration configureBundle(DropwizardBundleConfiguration configuration) {
    return configuration;
  }

  /**
   * Determines the start and stop strategy by looking up {@link StartAndStopStrategy} annotation.
   *
   * @return start and stop strategy to pe used. If null, bundle will be started and stopped for each test method.
   */
  protected Strategy getStartAndStopStrategy() {
    final StartAndStopStrategy strategy = getClass().getAnnotation(StartAndStopStrategy.class);
    if (strategy != null) {
      return strategy.value();
    }
    return Strategy.EACH_METHOD;
  }

}