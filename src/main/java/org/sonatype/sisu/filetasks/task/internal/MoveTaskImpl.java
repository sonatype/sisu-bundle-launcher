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
import org.sonatype.sisu.filetasks.task.RenameTask;

import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class MoveTaskImpl
    extends AbstractAntTask<Move>
    implements RenameTask
{

    private File target;

    private String name;

    @Override
    Class<Move> antTaskType()
    {
        return Move.class;
    }

    @Override
    void prepare( final Move move )
    {
        move.setFile( checkNotNull( target ) );
        move.setTofile( new File( target.getParentFile(), checkNotNull( name ) ) );

        move.setFailOnError( true );
    }

    @Override
    public RenameTask setTarget( final File target )
    {
        this.target = target;
        return this;
    }

    @Override
    public RenameTask setName( final String name )
    {
        this.name = name;
        return this;
    }
}
