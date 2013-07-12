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

import org.apache.tools.ant.taskdefs.Move;
import org.sonatype.sisu.filetasks.task.MoveTask;

import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link MoveTask} implementation.
 *
 * @since 1.0
 */
@Named
class MoveTaskImpl
    extends AbstractAntTask<Move>
    implements MoveTask
{

    /**
     * File/directory to be moved.
     */
    private File from;

    /**
     * Destination file/directory to move to.
     */
    private File to;

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
        move.setFile( checkNotNull( from ) );
        move.setTofile( checkNotNull( to ) );
        move.setFailOnError( true );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public MoveTask setFrom( final File from )
    {
        this.from = from;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public MoveTask setTo( final File to )
    {
        this.to = to;
        return this;
    }

}
