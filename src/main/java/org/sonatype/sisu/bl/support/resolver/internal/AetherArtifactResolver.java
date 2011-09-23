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

package org.sonatype.sisu.bl.support.resolver.internal;

import com.google.common.base.Preconditions;
import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.slf4j.Logger;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.connector.async.AsyncRepositoryConnectorFactory;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.inject.Nullable;
import org.sonatype.sisu.bl.support.resolver.ArtifactResolver;
import org.sonatype.sisu.bl.support.resolver.ResolvedArtifact;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Named("aether")
public class AetherArtifactResolver
        implements ArtifactResolver {

    @Inject
    private Logger log;

    @Inject
    @Named("${aether.updatePolicy:-daily}")
    private String updatePolicy;

    @Inject
    @Named("${aether.checksumPolicy:-fail}")
    private String checksumPolicy;

    @Inject
    @Named("${aether.repo.id:-rso}")
    private String repoId;

    @Inject
    @Named("${aether.repo.url:-https://repository.sonatype.org/content/groups/sonatype-public-grid/}")
    private String repoUrl;

    @Inject
    @Named("${aether.repo.type:-default}")
    private String repoType;

    private String localRepo;

    @Inject
    void setLocalRepo(@Nullable @Named("${aether.repo.local}") String localRepo) {
        this.localRepo = localRepo;
        if (localRepo == null || localRepo.length() <= 0) {
            String path = System.getProperty("maven.repo.local", "");
            if (path.length() > 0) {
                this.localRepo = new File(path).getAbsolutePath();
            } else {
                this.localRepo = new File(System.getProperty("user.home"), ".m2/repository").getAbsolutePath();
            }
        }
    }


    @Override
    public ResolvedArtifact resolveArtifact(final String coordinates) {
        Preconditions.checkNotNull(coordinates);
        List<String> coords = new ArrayList<String>();
        coords.add(coordinates);
        return resolveArtifacts(coords).iterator().next();
    }

    @Override
    public List<ResolvedArtifact> resolveArtifacts(Collection<String> coordinates) {
        DefaultServiceLocator locator = new DefaultServiceLocator();

        locator.addService(RepositoryConnectorFactory.class, AsyncRepositoryConnectorFactory.class);

        RepositorySystem repoSystem = locator.getService(RepositorySystem.class);

        MavenRepositorySystemSession repoSession = new MavenRepositorySystemSession();
        repoSession.setLocalRepositoryManager(repoSystem.newLocalRepositoryManager(new LocalRepository(localRepo)));
        repoSession.setTransferListener(new AetherTransferListener(log));
        repoSession.setUpdatePolicy(updatePolicy);
        repoSession.setChecksumPolicy(checksumPolicy);

        RemoteRepository remoteRepo = new RemoteRepository(repoId, repoType, repoUrl);

        List<ResolvedArtifact> artifacts = new ArrayList<ResolvedArtifact>();
        for (String coordinate : coordinates) {

            ArtifactRequest request = new ArtifactRequest();
            request.setArtifact(new DefaultArtifact(coordinate));
            request.addRepository(remoteRepo);
            try {
                ArtifactResult result = repoSystem.resolveArtifact(repoSession, request);
                artifacts.add(new DefaultResolvedArtifact(result.getArtifact()));
            } catch (ArtifactResolutionException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return artifacts;

    }


}
