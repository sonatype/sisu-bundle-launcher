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

package org.sonatype.sisu.bl;

import java.io.File;
import java.net.URL;

/**
 * Dropwizard bundle.
 *
 * @since 1.8
 */
public interface DropwizardBundle
    extends WebBundle<DropwizardBundle, DropwizardBundleConfiguration>
{

  /**
   * @return the admin port (random generated) application is running on. 0 (zero) if bundle is not running.
   */
  int getAdminPort();

  /**
   * @return URL admin application is available at. Null if bundle is not running.
   */
  URL getAdminUrl();

  /**
   * @return bundle directory (absolute path) of this bundle. Never null.
   */
  File getBundleDirectory();

}
