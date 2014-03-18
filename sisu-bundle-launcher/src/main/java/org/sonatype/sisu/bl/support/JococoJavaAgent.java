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

package org.sonatype.sisu.bl.support;

import java.io.File;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.JavaAgent;
import org.sonatype.sisu.bl.support.resolver.FileResolver;

import com.google.common.base.Preconditions;

/**
 * Jococo java agent.
 *
 * @since 1.8
 */
public class JococoJavaAgent
    implements JavaAgent
{

  private File jar;

  @Nullable
  private FileResolver jarResolver;

  @Override
  public String prepare(final Bundle bundle) {
    File jococoJar = getJar();
    if (jococoJar == null) {
      throw new IllegalStateException("Jococo jar not set");
    }
    return "-javaagent:" + jococoJar.getAbsolutePath() + "=" +
        "destfile=" + new File(bundle.getConfiguration().getTargetDirectory(), "jacoco.exec").getAbsolutePath();
  }

  @Inject
  public void setJarResolver(@Nullable @Named("jococo") FileResolver jarResolver) {
    this.jarResolver = jarResolver;
  }

  public void setJar(File jar) {
    this.jar = Preconditions.checkNotNull(jar, "jar may not be null");
  }

  public File getJar() {
    if (jar == null && jarResolver != null) {
      jar = jarResolver.resolve();
    }
    return jar;
  }

}
