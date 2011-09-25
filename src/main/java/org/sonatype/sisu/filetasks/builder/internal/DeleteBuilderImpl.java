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

import org.sonatype.sisu.filetasks.builder.DeleteBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
@Singleton
public class DeleteBuilderImpl
    implements DeleteBuilder
{

    private Provider<DeleteDirectoryBuilderImpl> deleteDirectoryBuilderProvider;

    private Provider<DeleteFileBuilderImpl> deleteFileBuilderProvider;

    @Inject
    DeleteBuilderImpl( Provider<DeleteDirectoryBuilderImpl> deleteDirectoryBuilderProvider,
                       Provider<DeleteFileBuilderImpl> deleteFileBuilderProvider )
    {
        this.deleteDirectoryBuilderProvider = checkNotNull( deleteDirectoryBuilderProvider );
        this.deleteFileBuilderProvider = checkNotNull( deleteFileBuilderProvider );
    }

    @Override
    public DeleteDirectoryBuilderImpl directory( final FileRef directory )
    {
        return deleteDirectoryBuilderProvider.get().directory( directory );
    }

    @Override
    public DeleteFileBuilderImpl file( final FileRef file )
    {
        return deleteFileBuilderProvider.get().file( file );
    }

}
