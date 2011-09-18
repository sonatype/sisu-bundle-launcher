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

import java.io.File;
import java.util.Map;

/**
 * @author plynch
 */
public interface ResolvedArtifact {

    public String getGroupId();

    public String getArtifactId();

    public String getVersion();

    public String getBaseVersion();

    public boolean isSnapshot();

    public String getClassifier();

    public String getExtension();

    public File getFile();

    public String getProperty(String key, String defaultValue);

    public Map<String, String> getProperties();
}
