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
package org.sonatype.sisu.bl;

import java.net.URL;

/**
 * An application bundle.
 *
 * @since 1.0
 */
public interface Bundle<T extends Bundle, C extends BundleConfiguration> {

    /**
     * Starts application created from this bundle, waiting for application to boot for a period of time specified by
     * configuration.
     *
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T start();

    /**
     * Stops a running application created from this bundle.
     *
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T stop();

    /**
     * Prepare file system to run by unpacking the configured bundle in target directory, applying
     * overlays. The resulted directory should be able to just be started by bundle specific launcher.
     *
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T prepare();

    /**
     * Cleans up filesystem, basically removing the target directory.
     *
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T cleanup() throws Exception;

    /**
     * Returns bundle configuration.
     *
     * @return bundle configuration, always a non null value
     * @since 1.0
     */
    C getConfiguration();

    /**
     * Sets bundle configuration.
     *
     * @param configuration configuration to be used. Can be null, case when a default configuration will be used
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T setConfiguration(C configuration);

    /**
     * Returns application running state.
     *
     * @return bundle running state, always a non null value
     * @since 1.0
     */
    State getState();

    /**
     * Information about running state of application.
     *
     * @since 1.0
     */
    interface State {

        /**
         * Returns true if application is running and false otherwise.
         *
         * @return true if application is running and false otherwise.
         * @since 1.0
         */
        boolean isRunning();

        /**
         * Returns the port (random generated) application is running on. If bundle is not running return 0 (zero).
         *
         * @return the port (random generated) application is running on. 0 (zero) if bundle is not running.
         * @since 1.0
         */
        int getPort();

        /**
         * Returns the URL application is available at. If bundle is not running returns null.
         *
         * @return URL application is available at. Null if bundle is not running.
         * @since 1.0
         */
        URL getUrl();

    }

}