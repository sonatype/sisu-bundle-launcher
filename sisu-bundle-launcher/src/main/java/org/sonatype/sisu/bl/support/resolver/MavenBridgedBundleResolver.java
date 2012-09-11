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
package org.sonatype.sisu.bl.support.resolver;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.inject.Nullable;
import org.sonatype.sisu.maven.bridge.MavenArtifactResolver;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.sisu.maven.bridge.support.ArtifactRequestBuilder.request;

import com.google.common.base.Throwables;

/**
 * {@link MavenArtifactResolver} based {@link BundleResolver}.
 * <p/>
 * Resolves the bundle specified by its Maven artifact coordinates.
 *
 * @since 1.0
 */
public class MavenBridgedBundleResolver
        implements BundleResolver {

    /**
     * Bundle coordinates configuration property key.
     */
    public static final String BUNDLE_COORDINATES = "DefaultBundleConfiguration.bundleCoordinates";

    /**
     * Maven coordinates of the bundle artifact to be resolved.
     */
    private final String bundleCoordinates;

    /**
     * Artifact resolver.
     */
    private final MavenArtifactResolver artifactResolver;

    /**
     *  Name of property used to lookup bundle coordinates.
     */
    private final String bundleCoordinatesProperty;

    /**
     * Constructor.
     *
     * @param bundleCoordinates Maven artifact coordinates of bundle to be resolved. If injected will use the
     *                          coordinates bounded to {@link #BUNDLE_COORDINATES}
     * @param artifactResolver  artifact resolver to be used to resolve the bundle
     */
    @Inject
    public MavenBridgedBundleResolver(final @Nullable @Named("${" + BUNDLE_COORDINATES + "}") String bundleCoordinates,
                                      final MavenArtifactResolver artifactResolver) {

        this(bundleCoordinates, BUNDLE_COORDINATES, artifactResolver);
    }

    /**
     * Constructor.
     *
     * @param bundleCoordinates Maven artifact coordinates of bundle to be resolved. If injected will use the
     *                          coordinates bounded to {@link #BUNDLE_COORDINATES}
     * @param bundleCoordinatesProperty name of property used to lookup bundle coordinates
     * @param artifactResolver  artifact resolver to be used to resolve the bundle
     */
    public MavenBridgedBundleResolver(final String bundleCoordinates,
                                      final String bundleCoordinatesProperty,
                                      final MavenArtifactResolver artifactResolver) {

        this.bundleCoordinates = bundleCoordinates;
        this.bundleCoordinatesProperty = bundleCoordinatesProperty;
        this.artifactResolver = checkNotNull(artifactResolver);
    }

    /**
     * Resolves bundle specified by Maven artifact coordinates using provided {@link MavenArtifactResolver}.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public File resolve() {
        if (bundleCoordinates == null) {
            throw new RuntimeException(
                "Bundle coordinates must be set. Did you set '" + bundleCoordinatesProperty + "' configuration property?");
        }
        try {
            Artifact artifact = artifactResolver.resolveArtifact(
                    request().artifact(bundleCoordinates)
            );
            return artifact.getFile();
        } catch (ArtifactResolutionException e) {
            throw Throwables.propagate( e );
        }
    }

}
