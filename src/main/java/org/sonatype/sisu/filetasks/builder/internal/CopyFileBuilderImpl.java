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

import org.sonatype.sisu.filetasks.builder.CopyFileBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.CopyFileTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class CopyFileBuilderImpl
    extends CopySettingsBuilderImpl<CopyFileBuilder, CopyFileTask>
    implements CopyFileBuilder
{

    private Retargetable source;

    private Retargetable targetFile;

    private Retargetable targetDirectory;

    @Inject
    CopyFileBuilderImpl( CopyFileTask copy )
    {
        super( copy );
        source = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFromFile( file );
            }

        } );
        targetFile = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setToFile( file );
            }

        } );
        targetDirectory = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setToDirectory( file );
            }

        } );
    }

    CopyFileBuilderImpl file( FileRef file )
    {
        source.setFileRef( file );
        return this;
    }

    @Override
    public DestinationBuilder to()
    {
        return new DestinationBuilder()
        {
            @Override
            public CopyFileBuilderImpl directory( final FileRef directory )
            {
                targetDirectory.setFileRef( directory );
                return CopyFileBuilderImpl.this;
            }

            @Override
            public CopyFileBuilderImpl file( final FileRef file )
            {
                targetFile.setFileRef( file );
                return CopyFileBuilderImpl.this;
            }
        };

    }

}
