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

package org.sonatype.sisu.filetasks.task.internal;

import org.apache.tools.ant.taskdefs.Move;
import org.sonatype.sisu.filetasks.task.MoveTask;

import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class MoveTaskImplfrom
    extends AbstractAntTask<Move>
    implements MoveTask
{

    private File from;

    private File to;

    @Override
    Class<Move> antTaskType()
    {
        return Move.class;
    }

    @Override
    void prepare( final Move move )
    {
        move.setFile( checkNotNull( from ) );
        move.setTofile( checkNotNull( to ) );

        move.setFailOnError( true );
    }

    @Override
    public MoveTask setFrom( final File from )
    {
        this.from = from;
        return this;
    }

    @Override
    public MoveTask setTo( final File target )
    {
        this.from = target;
        return this;
    }

}
