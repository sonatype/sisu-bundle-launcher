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

package org.sonatype.sisu.bl.internal.support.guice;

import javax.inject.Named;

import org.sonatype.sisu.maven.bridge.MavenArtifactResolver;
import org.sonatype.sisu.maven.bridge.support.artifact.RemoteMavenArtifactResolverUsingSettings;

import com.google.inject.AbstractModule;

@Named
public class GuiceModule
    extends AbstractModule
{

  @Override
  protected void configure() {
    bind(MavenArtifactResolver.class).to(RemoteMavenArtifactResolverUsingSettings.class);
  }

}
