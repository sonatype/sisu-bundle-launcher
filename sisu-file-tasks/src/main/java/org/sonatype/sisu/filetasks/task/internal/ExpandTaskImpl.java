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

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link ExpandTask} implementation.
 *
 * @since 1.0
 */
@Named
class ExpandTaskImpl
    extends AbstractAntTask<Expand>
    implements ExpandTask
{

    /**
     * Archive file to be expanded.
     */
    private File archive;

    /**
     * Destination directory where archive will be expanded.
     */
    private File toDirectory;

    /**
     * If a file which is newer in destination should be replaced. Default true.
     */
    private boolean overwriteNewer;

    /**
     * Number of directories from archive to cut while expanding. Defaults to zero = no directories will be cut.
     */
    private int directoriesToCut;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    ExpandTaskImpl()
    {
        overwriteNewer = true;
        directoriesToCut = 0;
    }

    /**
     * Returns a {@link Expand} or {@link Untar} ANT task based on archive extension.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Class<? extends Expand> antTaskType()
    {
        return checkNotNull( archive ).getName().endsWith( ".tar.gz" ) ? Untar.class : Expand.class;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Expand expand )
    {
        expand.setSrc( checkNotNull( archive ) );
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

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandTask setArchive( final File archive )
    {
        this.archive = archive;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandTask setToDirectory( final File directory )
    {
        this.toDirectory = directory;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandTaskImpl setOverwriteNewer( final boolean overwrite )
    {
        this.overwriteNewer = overwrite;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public ExpandTask setDirectoriesToCut( final int directoriesToCut )
    {
        this.directoriesToCut = directoriesToCut;
        return this;
    }

}
