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

import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Untar;
import org.apache.tools.ant.types.mappers.CutDirsMapper;
import org.sonatype.sisu.filetasks.task.ExpandTask;

import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class ExpandTaskImpl
    extends AbstractAntTask<Expand>
    implements ExpandTask
{

    private File archive;

    private File toDirectory;

    private boolean overwriteNewer;

    private int directoriesToCut;

    @Override
    Class<? extends Expand> antTaskType()
    {
        return checkNotNull( archive ).getName().endsWith( ".tar.gz" ) ? Untar.class : Expand.class;
    }

    @Override
    void prepare( final Expand expand )
    {
        expand.setSrc( archive );

        expand.setDest( checkNotNull( toDirectory ) );

        expand.setOverwrite( overwriteNewer );

        if ( directoriesToCut > 0 )
        {
            expand.add( new CutDirsMapper()
            {
                {
                    setDirs( directoriesToCut );
                }
            } );
        }

        expand.setStripAbsolutePathSpec( true );
    }

    @Override
    public ExpandTask setArchive( final File archive )
    {
        this.archive = archive;
        return this;
    }

    @Override
    public ExpandTask setToDirectory( final File directory )
    {
        this.toDirectory = directory;
        return this;
    }

    @Override
    public ExpandTaskImpl setOverwriteNewer( final boolean overwrite )
    {
        this.overwriteNewer = overwrite;
        return this;
    }

    @Override
    public ExpandTask setDirectoriesToCut( final int directoriesToCut )
    {
        this.directoriesToCut = directoriesToCut;
        return this;
    }

}
