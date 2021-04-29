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

package org.sonatype.sisu.bl.support.resolver;

import java.io.File;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.sisu.maven.bridge.MavenArtifactResolver;

import com.google.common.base.Throwables;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.sisu.maven.bridge.support.ArtifactRequestBuilder.request;

/**
 * {@link MavenArtifactResolver} based {@link FileResolver}.
 * 
 * Resolves the file specified by its Maven artifact coordinates.
 *
 * @since 1.8
 */
public class MavenBridgedFileResolver
    implements FileResolver
{

  /**
   * Maven coordinates of the artifact to be resolved.
   */
  private final String coordinates;

  /**
   * Artifact resolver.
   */
  private final MavenArtifactResolver artifactResolver;

  /**
   * Name of property used to lookup coordinates.
   */
  private final String coordinatesProperty;

  /**
   * Constructor.
   *
   * @param coordinates         Maven artifact coordinates of file to be resolved.
   * @param coordinatesProperty name of property used to lookup coordinates
   * @param artifactResolver    artifact resolver to be used to resolve the file
   */
  public MavenBridgedFileResolver(final String coordinates,
                                  final String coordinatesProperty,
                                  final MavenArtifactResolver artifactResolver)
  {

    this.coordinates = coordinates;
    this.coordinatesProperty = coordinatesProperty;
    this.artifactResolver = checkNotNull(artifactResolver);
  }

  /**
   * Resolves a file specified by Maven artifact coordinates using provided {@link MavenArtifactResolver}.
   * 
   * {@inheritDoc}
   */
  @Override
  public File resolve() {
    if (coordinates == null) {
      throw new RuntimeException(
          "Coordinates must be set. Did you set '" + coordinatesProperty + "' configuration property?");
    }
    try {
      Artifact artifact = artifactResolver.resolveArtifact(request().artifact(coordinates));
      return artifact.getFile();
    }
    catch (ArtifactResolutionException e) {
      throw Throwables.propagate(e);
    }
  }

}
