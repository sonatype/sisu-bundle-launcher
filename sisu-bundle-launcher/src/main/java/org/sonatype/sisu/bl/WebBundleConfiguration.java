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
 * An web bundle configuration.
 *
 * @since 1.2
 */
public interface WebBundleConfiguration<T extends WebBundleConfiguration>
    extends BundleConfiguration<T> {

    /**
     * Returns the port on which the application will be accessible.
     * If zero (0) a random port will be used.
     *
     * @return bundle identity
     */
    int getPort();

    /**
     * Sets the port on which the application will be accessible.
     * If zero (0) a random port will be used.
     *
     * @param port wanted port number
     * @return itself, for usage in fluent api
     */
    T setPort( int port );

}
