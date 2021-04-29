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
package org.sonatype.sisu.filetasks.task.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import javax.inject.Named;

import org.sonatype.sisu.filetasks.task.SymlinkTask;
import org.sonatype.sisu.filetasks.task.internal.ant.Symlink;

/**
 * ANT based {@link SymlinkTask} implementation.
 *
 * @since 1.0
 */
@Named
class SymlinkTaskImpl
    extends AbstractAntTask<Symlink>
    implements SymlinkTask
{

    /**
     * File/directory to be linked.
     */
    private File source;

    /**
     * Symlink to be created.
     */
    private File target;

    /**
     * True if a hard link should be created.
     */
    private boolean hardLink;

    /**
     * Returns a {@link Symlink} ANT task.
     * 
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Class<Symlink> antTaskType()
    {
        return Symlink.class;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Symlink symlink )
    {
        symlink.setAction( "single" );
        symlink.setResource( checkNotNull( source ).getAbsolutePath() );
        symlink.setLink( checkNotNull( target ).getAbsolutePath() );
        symlink.setHardlink( hardLink );
        symlink.setFailOnError( true );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public SymlinkTask setSource( final File source )
    {
        this.source = source;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public SymlinkTask setTarget( final File target )
    {
        this.target = target;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public SymlinkTask setHardLink( final boolean hardLink )
    {
        this.hardLink = hardLink;
        return this;
    }

}
