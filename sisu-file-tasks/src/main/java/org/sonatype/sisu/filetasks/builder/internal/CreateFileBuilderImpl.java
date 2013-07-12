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
package org.sonatype.sisu.filetasks.builder.internal;

import java.io.File;
import javax.inject.Inject;
import static com.google.common.base.Preconditions.checkNotNull;
import javax.inject.Named;
import org.sonatype.sisu.filetasks.builder.CreateFileBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.CreateFileTask;

/**
 * TODO
 *
 * @since 1.10
 */
@Named
class CreateFileBuilderImpl extends BuilderImpl implements CreateFileBuilder {

    private final CreateFileTask createFileTask;

    /**
     * Re-targetable directory where to create the file.
     */
    private Retargetable target;


    @Inject
    public CreateFileBuilderImpl(final CreateFileTask createFileTask) {
        this.createFileTask = checkNotNull(createFileTask);
        target = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFile( file );
            }

        } );
    }

    CreateFileBuilder file(final FileRef file) {
        target.setFileRef( file );
        return this;
    }

    @Override
    public CreateFileBuilder containing(final String content) {
        createFileTask.setContent(content);
        return this;
    }

    @Override
    CreateFileTask task() {
        return createFileTask;
    }

    @Override
    public CreateFileBuilder encodedAs(final String encoding) {
        createFileTask.setEncoding(encoding);
        return this;
    }


}
