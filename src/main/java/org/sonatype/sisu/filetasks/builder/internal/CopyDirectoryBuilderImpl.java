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

import org.sonatype.sisu.filetasks.builder.CopyDirectoryBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.task.CopyDirectoryTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class CopyDirectoryBuilderImpl
    extends CopySettingsBuilderImpl<CopyDirectoryBuilder, CopyDirectoryTask>
    implements CopyDirectoryBuilder
{

    private Retargetable from;

    private Retargetable to;

    @Inject
    CopyDirectoryBuilderImpl( CopyDirectoryTask copy )
    {
        super( copy );
        from = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFromDirectory( file );
            }

        } );
        to = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setToDirectory( file );
            }

        } );

    }

    CopyDirectoryBuilderImpl directory( FileRef directory )
    {
        from.setFileRef( directory );
        return this;
    }

    @Override
    public CopyDirectoryBuilderImpl include( final String pattern )
    {
        task().addIncludePattern( pattern );
        return this;
    }

    @Override
    public CopyDirectoryBuilderImpl exclude( final String pattern )
    {
        task().addExcludePattern( pattern );
        return this;
    }

    @Override
    public CopyDirectoryBuilderImpl includeEmptyDirectories()
    {
        task().setIncludeEmptyDirectories( true );
        return this;
    }

    @Override
    public CopyDirectoryBuilderImpl excludeEmptyDirectories()
    {
        task().setIncludeEmptyDirectories( false );
        return this;
    }

    @Override
    public PathBuilder to()
    {
        return new PathBuilder()
        {
            @Override
            public CopyDirectoryBuilderImpl directory( final FileRef directory )
            {
                to.setFileRef( directory );
                return CopyDirectoryBuilderImpl.this;
            }
        };
    }

}
