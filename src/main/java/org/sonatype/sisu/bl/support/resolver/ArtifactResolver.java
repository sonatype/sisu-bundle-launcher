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

package org.sonatype.sisu.bl.support.resolver;

import java.util.Collection;
import java.util.List;

/**
 * Resolves artifacts from Maven repositories.
 */
public interface ArtifactResolver {

    /**
     * Resolves an artifact using specified artifact coordinates.
     *
     * @param coordinate The artifact coordinates in the format
     *                   {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     * @return immutable resolved artifact, never {@code null}.
     */
    ResolvedArtifact resolveArtifact(String coordinate);

    /**
     * Resolves artifacts using the set of specified artifact coordinates.
     *
     * @param coordinates A Set of artifact coordinates in the format
     *                    {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     * @return immutable set of resolved artifacts, never {@code null}.
     */
    List<ResolvedArtifact> resolveArtifacts(Collection<String> coordinates);

}
