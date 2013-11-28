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
package org.sonatype.sisu.bl.servlet.tomcat.internal;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.bl.support.resolver.MavenBridgedBundleResolver;
import org.sonatype.sisu.maven.bridge.MavenArtifactResolver;

/**
 * Tomcat bundle resolver.
 *
 * @since 1.0
 */
@Named
public class TomcatBundleResolver
    extends MavenBridgedBundleResolver
{

    /**
     * Bundle coordinates configuration property key.
     */
    public static final String BUNDLE_COORDINATES = "TomcatBundleConfiguration.bundleCoordinates";

    /**
     * Constructor.
     *
     * @param bundleCoordinates Maven artifact coordinates of bundle to be resolved. If injected will use the
     *                          coordinates bounded to {@link #BUNDLE_COORDINATES}
     * @param artifactResolver  artifact resolver to be used to resolve the bundle
     * @since 2.0
     */
    @Inject
    public TomcatBundleResolver( final @Nullable @Named( "${" + BUNDLE_COORDINATES + "}" ) String bundleCoordinates,
                                 final MavenArtifactResolver artifactResolver )
    {
        super( bundleCoordinates, artifactResolver );
    }

}
