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

import org.sonatype.sisu.filetasks.builder.CopyBuilder;
import org.sonatype.sisu.filetasks.builder.CopyDirectoryBuilder;
import org.sonatype.sisu.filetasks.builder.CopyFileBuilder;
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
public class CopyBuilderImpl
    implements CopyBuilder
{

    private Provider<CopyDirectoryBuilderImpl> copyDirectoryBuilderProvider;

    private Provider<CopyFileBuilderImpl> copyFileBuilderProvider;

    /**
     * Constructor.
     *
     * @param copyDirectoryBuilderProvider ongoing copy directory builder provider
     * @param copyFileBuilderProvider      ongoing copy file builderprovider
     * @since 1.0
     */
    @Inject
    CopyBuilderImpl( Provider<CopyDirectoryBuilderImpl> copyDirectoryBuilderProvider,
                     Provider<CopyFileBuilderImpl> copyFileBuilderProvider )
    {
        this.copyDirectoryBuilderProvider = checkNotNull( copyDirectoryBuilderProvider );
        this.copyFileBuilderProvider = checkNotNull( copyFileBuilderProvider );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyDirectoryBuilder directory( final FileRef directory )
    {
        return copyDirectoryBuilderProvider.get().directory( directory );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyFileBuilder file( final FileRef file )
    {
        return copyFileBuilderProvider.get().file( file );
    }

}
