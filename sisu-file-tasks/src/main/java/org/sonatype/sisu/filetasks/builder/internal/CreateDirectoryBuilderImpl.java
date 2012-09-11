/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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
import static com.google.common.base.Preconditions.checkNotNull;
import javax.inject.Named;
import org.sonatype.sisu.filetasks.builder.CreateDirectoryBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.CreateDirectoryTask;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class CreateDirectoryBuilderImpl
    extends BuilderImpl
    implements CreateDirectoryBuilder {

    private final CreateDirectoryTask createDirectoryTask;

    /**
     * Re-targetable directory where to create the file.
     */
    private Retargetable target;


    @Inject
    public CreateDirectoryBuilderImpl(final CreateDirectoryTask createDirectoryTask) {
        this.createDirectoryTask = checkNotNull(createDirectoryTask);
        target = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setDirectory( file );
            }

        } );
    }

    CreateDirectoryBuilder directory(final FileRef fileref) {
        target.setFileRef(fileref);
        return this;
    }

    @Override
    CreateDirectoryTask task() {
        return createDirectoryTask;
    }


}
