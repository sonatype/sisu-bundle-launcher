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
package org.sonatype.sisu.bl;

/**
 * An application bundle.
 *
 * @since 1.0
 */
public interface Bundle<B extends Bundle, BC extends BundleConfiguration> {

    /**
     * Starts application created from this bundle, waiting for application to boot for a period of time specified by
     * configuration.
     *
     * @return itself, for usage in fluent api
     */
    B start();

    /**
     * Stops a running application created from this bundle.
     *
     * @return itself, for usage in fluent api
     */
    B stop();

    /**
     * Prepare file system to run by unpacking the configured bundle in target directory, applying
     * overlays. The resulted directory should be able to just be started by bundle specific launcher.
     *
     * @return itself, for usage in fluent api
     */
    B prepare();

    /**
     * Cleans up filesystem, basically removing the target directory.
     *
     * @return itself, for usage in fluent api
     */
    B cleanup() throws Exception;

    /**
     * Returns bundle configuration.
     *
     * @return bundle configuration, always a non null value
     */
    BC getConfiguration();

    /**
     * Sets bundle configuration.
     *
     * @param configuration configuration to be used. Can be null, case when a default configuration will be used
     * @return itself, for usage in fluent api
     */
    B setConfiguration(BC configuration);


    // TODO Use {@link #isStarted} or {@link #isReady} concepts instead, because running is vague
     /**
     * Indicate if application has started it's boot cycle.
     *
     * @return true if application has started it's boot cycle.
     */
    boolean isRunning();

    /**
     * Returns statistics about this bundle.
     * @return statistics
     *
     * @since 1.5
     */
    BundleStatistics statistics();

}
