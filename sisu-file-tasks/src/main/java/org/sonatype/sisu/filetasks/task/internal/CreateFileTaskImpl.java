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

package org.sonatype.sisu.filetasks.task.internal;

import com.google.common.io.Files;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

import java.nio.charset.Charset;
import org.sonatype.sisu.filetasks.task.CreateFileTask;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link CreateFileTask} implementation.
 *
 * @since 1.0
 */
@Named
class CreateFileTaskImpl
    implements CreateFileTask
{
    private String encoding;
    private String content;
    private File fileToCreate;

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CreateFileTask setFile( final File file )
    {
        this.fileToCreate = file;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CreateFileTask setEncoding( final String encoding )
    {
        this.encoding = encoding;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CreateFileTask setContent( final String content )
    {
        this.content = content;
        return this;
    }


    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    CreateFileTaskImpl()
    {
    }


    @Override
    public void run() {
        checkNotNull( fileToCreate );
        try {
            Files.createParentDirs(fileToCreate);
            fileToCreate.createNewFile();
            if ( content != null ){
                Files.append(content, fileToCreate, encoding == null ? Charset.defaultCharset() : Charset.forName(encoding));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not create file", ex);
        }
    }

}
