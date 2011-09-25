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

import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.MoveBuilder;
import org.sonatype.sisu.filetasks.task.MoveTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
public class MoveBuilderImpl
    extends BuilderImpl
    implements MoveBuilder
{

    private MoveTask task;

    private Retargetable from;

    private Retargetable to;

    @Inject
    MoveBuilderImpl( MoveTask task )
    {
        this.task = task;
        from = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setFrom( file );
            }

        } );
        to = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setTo( file );
            }

        } );
    }

    public MoveBuilderImpl from( final FileRef from )
    {
        this.from.setFileRef( from );
        return this;
    }

    @Override
    public MoveBuilderImpl to( final FileRef to )
    {
        this.to.setFileRef( to );
        return this;
    }

    MoveTask task()
    {
        return task;
    }

}
