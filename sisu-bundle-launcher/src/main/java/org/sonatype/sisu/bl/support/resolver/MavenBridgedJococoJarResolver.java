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

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.maven.bridge.MavenArtifactResolver;

/**
 * {@link MavenArtifactResolver} based Jococo {@link FileResolver}.
 * <p/>
 * Resolves the jar specified by its Maven artifact coordinates.
 *
 * @since 1.8
 */
public class MavenBridgedJococoJarResolver
    extends MavenBridgedFileResolver
{

  /**
   * Coordinates configuration property key.
   */
  public static final String COORDINATES = "jococo.jar.coordinates";

  /**
   * Constructor.
   *
   * @param coordinates      Maven artifact coordinates of jococo jar to be resolved. If injected will use the
   *                         coordinates bounded to {@link #COORDINATES}
   * @param artifactResolver artifact resolver to be used to resolve the bundle
   */
  @Inject
  public MavenBridgedJococoJarResolver(final @Nullable @Named("${" + COORDINATES + "}") String coordinates,
                                       final MavenArtifactResolver artifactResolver)
  {
    super(coordinates, COORDINATES, artifactResolver);
  }

}
