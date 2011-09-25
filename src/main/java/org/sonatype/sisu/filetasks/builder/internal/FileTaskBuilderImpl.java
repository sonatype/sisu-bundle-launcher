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

import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.builder.ChmodBuilder;
import org.sonatype.sisu.filetasks.builder.CopyBuilder;
import org.sonatype.sisu.filetasks.builder.DeleteBuilder;
import org.sonatype.sisu.filetasks.builder.ExpandBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.MoveBuilder;
import org.sonatype.sisu.filetasks.builder.PropertiesBuilder;
import org.sonatype.sisu.filetasks.builder.RenameBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * TODO
 * ]
 *
 * @since 1.0
 */
@Named
@Singleton
class FileTaskBuilderImpl
    implements FileTaskBuilder
{

    private CopyBuilder copyBuilder;

    private DeleteBuilder deleteBuilder;

    private Provider<RenameBuilderImpl> renameBuilderProvider;

    private Provider<MoveBuilderImpl> moveBuilderProvider;

    private Provider<ExpandBuilderImpl> expandBuilderProvider;

    private Provider<PropertiesBuilderImpl> propertiesBuilderProvider;

    private Provider<ChmodBuilderImpl> chmodBuilderProvider;

    @Inject
    FileTaskBuilderImpl( final CopyBuilder copyBuilder,
                         final DeleteBuilder deleteBuilder,
                         final Provider<RenameBuilderImpl> renameBuilderProvider,
                         final Provider<MoveBuilderImpl> moveBuilderProvider,
                         final Provider<ExpandBuilderImpl> expandBuilderProvider,
                         final Provider<PropertiesBuilderImpl> propertiesBuilderProvider,
                         final Provider<ChmodBuilderImpl> chmodBuilderProvider )
    {
        this.copyBuilder = copyBuilder;
        this.deleteBuilder = deleteBuilder;
        this.renameBuilderProvider = renameBuilderProvider;
        this.moveBuilderProvider = moveBuilderProvider;
        this.expandBuilderProvider = expandBuilderProvider;
        this.propertiesBuilderProvider = propertiesBuilderProvider;
        this.chmodBuilderProvider = chmodBuilderProvider;
    }

    @Override
    public CopyBuilder copy()
    {
        return copyBuilder;
    }

    @Override
    public DeleteBuilder delete()
    {
        return deleteBuilder;
    }

    @Override
    public RenameBuilder rename( final FileRef target )
    {
        return renameBuilderProvider.get().target( target );
    }

    @Override
    public MoveBuilder move( final FileRef from )
    {
        return moveBuilderProvider.get().from( from );
    }

    @Override
    public ExpandBuilder expand( FileRef archive )
    {
        return expandBuilderProvider.get().archive( archive );
    }

    @Override
    public PropertiesBuilder properties( final FileRef target )
    {
        return propertiesBuilderProvider.get().file( target );
    }

    @Override
    public ChmodBuilder chmod( final FileRef directory )
    {
        return chmodBuilderProvider.get().directory( directory );
    }

}
