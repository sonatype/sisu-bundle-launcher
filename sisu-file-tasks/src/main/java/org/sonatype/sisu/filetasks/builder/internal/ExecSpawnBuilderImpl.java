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

package org.sonatype.sisu.filetasks.builder.internal;

import java.io.File;
import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.sisu.filetasks.builder.CopyFileBuilder;
import org.sonatype.sisu.filetasks.builder.ExecSpawnBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.CopyFileTask;
import org.sonatype.sisu.filetasks.task.ExecSpawnTask;

/**
 * {@link ExecSpawnBuilder} implementation.
 *
 * @since 1.3
 */
@Named
class ExecSpawnBuilderImpl
    extends ExecSettingsBuilderImpl<ExecSpawnBuilder, ExecSpawnTask>
    implements ExecSpawnBuilder
{

    @Inject
    ExecSpawnBuilderImpl( final ExecSpawnTask task )
    {
        super( task );
    }

}
