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

import java.net.URL;

/**
 * An web accessible application bundle.
 *
 * @since 1.0
 */
public interface WebBundle<WB extends WebBundle, BC extends BundleConfiguration>
    extends Bundle<WB,BC>{

    /**
     * Returns the port (random generated) application is running on. If bundle is not running return 0 (zero).
     *
     * @return the port (random generated) application is running on. 0 (zero) if bundle is not running.
     */
    int getPort();

    /**
     * Returns the URL application is available at. If bundle is not running returns null.
     *
     * @return URL application is available at. Null if bundle is not running.
     */
    URL getUrl();

}