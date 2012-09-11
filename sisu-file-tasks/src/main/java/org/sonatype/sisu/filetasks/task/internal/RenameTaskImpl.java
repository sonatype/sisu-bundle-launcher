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
package org.sonatype.sisu.filetasks.task.internal;

import org.apache.tools.ant.taskdefs.Move;
import org.sonatype.sisu.filetasks.task.RenameTask;

import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link RenameTask} implementation.
 *
 * @since 1.0
 */
@Named
class RenameTaskImpl
    extends AbstractAntTask<Move>
    implements RenameTask
{

    /**
     * File/directory to be renamed.
     */
    private File target;

    /**
     * New name.
     */
    private String name;

    /**
     * Returns a {@link Move} ANT task.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Class<Move> antTaskType()
    {
        return Move.class;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Move move )
    {
        move.setFile( checkNotNull( target ) );
        move.setTofile( new File( target.getParentFile(), checkNotNull( name ) ) );
        move.setFailOnError( true );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public RenameTask setTarget( final File target )
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
    public RenameTask setName( final String name )
    {
        this.name = name;
        return this;
    }

}
