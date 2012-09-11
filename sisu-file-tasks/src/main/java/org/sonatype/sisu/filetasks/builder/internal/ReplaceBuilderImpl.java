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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.ReplaceBuilder;
import org.sonatype.sisu.filetasks.builder.ReplaceInFileBuilder;
import org.sonatype.sisu.filetasks.builder.ReplaceInFilesFromDirectoryBuilder;

/**
 * Builder for replacing values in files.
 *
 * @since 1.3
 */
@Named
@Singleton
public class ReplaceBuilderImpl
    implements ReplaceBuilder
{

    private Provider<ReplaceInFilesFromDirectoryBuilderImpl> directoryBuilderProvider;

    private Provider<ReplaceInFileBuilderImpl> fileBuilderProvider;

    /**
     * Constructor.
     *
     * @param fileBuilderProvider ongoing replace in files from directory builder provider
     * @param fileBuilderProvider ongoing replace in file builder provider
     */
    @Inject
    ReplaceBuilderImpl( final Provider<ReplaceInFilesFromDirectoryBuilderImpl> directoryBuilderProvider,
                        final Provider<ReplaceInFileBuilderImpl> fileBuilderProvider )
    {
        this.directoryBuilderProvider = checkNotNull( directoryBuilderProvider );
        this.fileBuilderProvider = checkNotNull( fileBuilderProvider );
    }

    @Override
    public ReplaceInFilesFromDirectoryBuilder inFilesFromDirectory( final FileRef directory )
    {
        return directoryBuilderProvider.get().directory( directory );
    }

    @Override
    public ReplaceInFileBuilder inFile( final FileRef file )
    {
        return fileBuilderProvider.get().file( file );
    }
}
