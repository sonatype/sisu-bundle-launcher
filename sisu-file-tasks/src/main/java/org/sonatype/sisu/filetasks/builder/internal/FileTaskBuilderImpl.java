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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.builder.ChmodBuilder;
import org.sonatype.sisu.filetasks.builder.CopyBuilder;
import org.sonatype.sisu.filetasks.builder.CreateBuilder;
import org.sonatype.sisu.filetasks.builder.DeleteBuilder;
import org.sonatype.sisu.filetasks.builder.ExecBuilder;
import org.sonatype.sisu.filetasks.builder.ExpandBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.MoveBuilder;
import org.sonatype.sisu.filetasks.builder.PropertiesBuilder;
import org.sonatype.sisu.filetasks.builder.RenameBuilder;
import org.sonatype.sisu.filetasks.builder.ReplaceBuilder;
import org.sonatype.sisu.filetasks.builder.SymlinkBuilder;
import org.sonatype.sisu.filetasks.builder.WarBuilder;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
@Singleton
class FileTaskBuilderImpl
    implements FileTaskBuilder
{

    /**
     * Copy builder.
     */
    private CopyBuilder copyBuilder;

    /**
     * Create builder.
     */
    private CreateBuilder createBuilder;

    /**
     * Create builder.
     */
    private Provider<WarBuilderImpl> warBuilderProvider;

    /**
     * Delete builder.
     */
    private DeleteBuilder deleteBuilder;

    /**
     * Rename builder provider.
     */
    private Provider<RenameBuilderImpl> renameBuilderProvider;

    /**
     * Move builder provider.
     */
    private Provider<MoveBuilderImpl> moveBuilderProvider;

    /**
     * Symlink builder provider.
     */
    private Provider<SymlinkBuilderImpl> symlinkBuilderProvider;

    /**
     * Expand builder provider.
     */
    private Provider<ExpandBuilderImpl> expandBuilderProvider;

    /**
     * Properties builder provider.
     */
    private Provider<PropertiesBuilderImpl> propertiesBuilderProvider;

    /**
     * Chmod builder provider.
     */
    private Provider<ChmodBuilderImpl> chmodBuilderProvider;

    /**
     * Exec builder.
     */
    private ExecBuilder execBuilder;

    /**
     * Replace builder.
     */
    private ReplaceBuilder replaceBuilder;

    /**
     * Constructor.
     *
     * @param copyBuilder               copy builder
     * @param deleteBuilder             delete builder
     * @param renameBuilderProvider     rename builder provider
     * @param moveBuilderProvider       move builder provider
     * @param expandBuilderProvider     expand builder provider
     * @param propertiesBuilderProvider properties builder provider
     * @param chmodBuilderProvider      chmod builder provider
     * @param execBuilder               exec builder
     * @since 1.0
     */
    @Inject
    FileTaskBuilderImpl( final CopyBuilder copyBuilder,
                         final CreateBuilder createBuilder,
                         final DeleteBuilder deleteBuilder,
                         final Provider<WarBuilderImpl> warBuilderProvider,
                         final Provider<RenameBuilderImpl> renameBuilderProvider,
                         final Provider<MoveBuilderImpl> moveBuilderProvider,
                         final Provider<SymlinkBuilderImpl> symlinkBuilderProvider,
                         final Provider<ExpandBuilderImpl> expandBuilderProvider,
                         final Provider<PropertiesBuilderImpl> propertiesBuilderProvider,
                         final Provider<ChmodBuilderImpl> chmodBuilderProvider,
                         final ExecBuilder execBuilder,
                         final ReplaceBuilder replaceBuilder )
    {
        this.copyBuilder = checkNotNull( copyBuilder );
        this.createBuilder = checkNotNull( createBuilder );
        this.warBuilderProvider = checkNotNull( warBuilderProvider );
        this.deleteBuilder = checkNotNull( deleteBuilder );
        this.renameBuilderProvider = checkNotNull( renameBuilderProvider );
        this.moveBuilderProvider = checkNotNull( moveBuilderProvider );
        this.symlinkBuilderProvider = checkNotNull( symlinkBuilderProvider );
        this.expandBuilderProvider = checkNotNull( expandBuilderProvider );
        this.propertiesBuilderProvider = checkNotNull( propertiesBuilderProvider );
        this.chmodBuilderProvider = checkNotNull( chmodBuilderProvider );
        this.execBuilder = checkNotNull( execBuilder );
        this.replaceBuilder = checkNotNull( replaceBuilder );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CopyBuilder copy()
    {
        return copyBuilder;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public CreateBuilder create()
    {
        return createBuilder;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public WarBuilder war( final FileRef archive )
    {
        return warBuilderProvider.get().archive( archive );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DeleteBuilder delete()
    {
        return deleteBuilder;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public RenameBuilder rename( final FileRef target )
    {
        return renameBuilderProvider.get().target( target );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public MoveBuilder move( final FileRef from )
    {
        return moveBuilderProvider.get().from( from );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public SymlinkBuilder symlink( final FileRef source )
    {
        return symlinkBuilderProvider.get().source( source );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandBuilder expand( FileRef archive )
    {
        return expandBuilderProvider.get().archive( archive );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public PropertiesBuilder properties( final FileRef target )
    {
        return propertiesBuilderProvider.get().file( target );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ChmodBuilder chmod( final FileRef directory )
    {
        return chmodBuilderProvider.get().directory( directory );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.3
     */
    @Override
    public ExecBuilder exec()
    {
        return execBuilder;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.3
     */
    @Override
    public ReplaceBuilder replace()
    {
        return replaceBuilder;
    }

}
