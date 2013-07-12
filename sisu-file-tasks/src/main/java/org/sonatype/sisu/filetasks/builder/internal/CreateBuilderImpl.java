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

import org.sonatype.sisu.filetasks.builder.FileRef;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.sonatype.sisu.filetasks.builder.CreateBuilder;
import org.sonatype.sisu.filetasks.builder.CreateDirectoryBuilder;
import org.sonatype.sisu.filetasks.builder.CreateFileBuilder;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builder for creating directories and files.
 *
 * @since 1.0
 */
@Named
@Singleton
public class CreateBuilderImpl
    implements CreateBuilder
{

    private Provider<CreateDirectoryBuilderImpl> createDirectoryBuilderProvider;

    private Provider<CreateFileBuilderImpl> createFileBuilderProvider;

    /**
     * Constructor.
     *
     * @param createDirectoryBuilderProvider ongoing create directory builder provider
     * @param createFileBuilderProvider      ongoing create file builder provider
     * @since 1.0
     */
    @Inject
    CreateBuilderImpl( Provider<CreateDirectoryBuilderImpl> createDirectoryBuilderProvider,
                     Provider<CreateFileBuilderImpl> createFileBuilderProvider )
    {
        this.createDirectoryBuilderProvider = checkNotNull( createDirectoryBuilderProvider );
        this.createFileBuilderProvider = checkNotNull( createFileBuilderProvider );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CreateDirectoryBuilder directory( final FileRef directory )
    {
        return createDirectoryBuilderProvider.get().directory( directory );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CreateFileBuilder file( final FileRef file )
    {
        return createFileBuilderProvider.get().file( file );
    }

}
