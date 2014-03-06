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

/**
 * {@link DropwizardBundle} configuration.
 *
 * @since 1.8
 */
public interface DropwizardBundleConfiguration
    extends WebBundleConfiguration<DropwizardBundleConfiguration>
{

  /**
   * @return the port on which the application will be accessible. If zero (0) a random port will be used.
   */
  int getAdminPort();

  /**
   * Sets the port on which the application will be accessible. If zero (0) a random port will be used.
   *
   * @param port wanted port number
   * @return itself, for usage in fluent api
   */
  DropwizardBundleConfiguration setAdminPort(int port);

  /**
   * @return application jar name. If not set and bundle contains only one jar, it will be used.
   */
  String jarName();

  /**
   * @param jarName application jar name (should not be null)
   * @return itself, for usage in fluent api
   */
  DropwizardBundleConfiguration setJarName(String jarName);

  /**
   * @return Yaml file used to configure the application (should not be null).
   */
  File getYaml();

  /**
   * @param yaml Yaml file used to configure the application. (should not be null)
   * @return itself, for usage in fluent api
   */
  DropwizardBundleConfiguration setYaml(File yaml);

  /**
   * @return arguments to be passed to application (used as {@code java -jar <jar name> <arguments> config.yaml})
   *         (should not be null)
   */
  String[] arguments();

  /**
   * @param arguments to be passed to application (used as {@code java -jar <jar name> <arguments> config.yaml})
   * @return itself, for usage in fluent api
   */
  DropwizardBundleConfiguration setArguments(String... arguments);

}
