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

import org.sonatype.sisu.overlay.Overlay;

import java.io.File;
import java.util.List;

/**
 * An bundle configuration.
 *
 * @since 1.0
 */
public interface BundleConfiguration<T extends BundleConfiguration> {

    /**
     * Returns bundle identity.
     *
     * @return bundle identity
     * @since 1.0
     */
    String getId();

    /**
     * Sets bundle identity.
     *
     * @param id bundle identity
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T setId(String id);

    /**
     * Returns the bundle assembly (zip or tar).
     *
     * @return bundle assembly file
     * @since 1.0
     */
    File getBundle();

    /**
     * Sets bundle assembly.
     *
     * @param bundle a zip/jar/tar file or a directory.
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T setBundle(File bundle);

    /**
     * Returns the target directory to be used for exploding the bundle.
     *
     * @return target directory to be used for exploding the bundle.
     * @since 1.0
     */
    File getTargetDirectory();

    /**
     * Sets target directory.
     *
     * @param targetDirectory directory where bundle will be exploded
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T setTargetDirectory(File targetDirectory);

    /**
     * Returns overlays to be applied over exploded bundle.
     *
     * @return overlays to be applied over exploded bundle, always a non null value (eventually empty)
     * @since 1.0
     */
    List<Overlay> getOverlays();

    /**
     * Sets overlays. Provided overlays will overwrite existing overlays.
     * Overlays are applied in provided order.
     *
     * @param overlays overlays to be applied over exploded bundle. Can be null, case when an empty list will be
     *                 used
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T setOverlays(List<Overlay> overlays);

    /**
     * Sets overlays. Provided overlays will overwrite existing overlays.
     * Overlays are applied in provided order.
     *
     * @param overlays overlays to be applied over exploded bundle
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T setOverlays(Overlay... overlays);

    /**
     * Append overlays to existing set of overlays.
     *
     * @param overlays overlays to be applied over exploded bundle
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T addOverlays(Overlay... overlays);

    /**
     * Returns the number of seconds to wait for application to start (boot).
     *
     * @return the number of seconds to wait for application to start (boot)
     * @since 1.0
     */
    Integer getStartTimeout();

    /**
     * Sets start (boot) timeout.
     *
     * @param timeout the number of seconds to wait for application to start (boot)
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    T setStartTimeout(Integer timeout);

}