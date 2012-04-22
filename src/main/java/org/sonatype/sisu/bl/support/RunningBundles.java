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
package org.sonatype.sisu.bl.support;

import org.sonatype.sisu.bl.Bundle;

/**
 * Keeps a list of current running bundles.
 *
 * @since 1.0
 */
public interface RunningBundles
{

    /**
     * Adds a running bundle to the list of running bundles.
     *
     * @param bundle to be added
     * @since 1.0
     */
    void add( Bundle bundle );

    /**
     * Removes a running bundle from the list of running bundles.
     * If bundle was not previously added, returns silently.
     *
     * @param bundle to be removed
     * @since 1.0
     */
    void remove( Bundle bundle );

    /**
     * Returns a copy of the list of running bundles at the time of call.
     *
     * @return running bundles
     * @since 1.0
     */
    Bundle[] get();

    /**
     * Returns a copy of the list of running bundles at the time of call of the type specified.
     *
     * @param bundleType type of the bundles to be included
     * @return running bundles
     * @since 1.0
     */
    Bundle[] get( Class<?> bundleType );

}
