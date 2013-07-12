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
package org.sonatype.sisu.filetasks.builder;

import java.io.File;
import org.sonatype.sisu.filetasks.FileTask;

/**
 * {@link org.sonatype.sisu.filetasks.task.WarTask} builder.
 *
 * @since 1.0
 */
public interface WarBuilder
    extends FileTask
{

    /**
     * Add libraries matching libIncludes in libDir to the war archive
     * @param libDir the root directory to add libs from
     * @param libIncludes the includes pattern to select the libs to add
     * @return self, for fluent API usage
     */
    WarBuilder addLibs(File libDir, String libIncludes);

}
