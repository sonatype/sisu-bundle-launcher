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
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.sisu.bl.support.resolver.ResolvedArtifact;

import java.io.File;
import java.util.Collections;
import java.util.Map;

/**
 * Immutable resolved artifact.
 *
 * @since 1.9.3
 */
public class DefaultResolvedArtifact implements ResolvedArtifact {

    DefaultResolvedArtifact(final Artifact artifact) {
        Preconditions.checkNotNull(artifact);
        this.artifact = artifact;
    }

    private Artifact artifact;

    @Override
    public String getGroupId() {
        return this.artifact.getGroupId();
    }

    @Override
    public String getArtifactId() {
        return this.artifact.getArtifactId();
    }

    @Override
    public String getVersion() {
        return this.artifact.getVersion();
    }

    @Override
    public String getBaseVersion() {
        return this.artifact.getBaseVersion();
    }

    @Override
    public boolean isSnapshot() {
        return this.artifact.isSnapshot();
    }

    @Override
    public String getClassifier() {
        return this.artifact.getClassifier();
    }

    @Override
    public String getExtension() {
        return this.artifact.getExtension();
    }

    @Override
    public File getFile() {
        return this.artifact.getFile();
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return this.artifact.getProperty(key, defaultValue);
    }

    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(this.artifact.getProperties());
    }

}
