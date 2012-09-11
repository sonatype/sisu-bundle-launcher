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

import org.sonatype.sisu.filetasks.task.ExpandTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.taskdefs.War;
import org.apache.tools.ant.types.ZipFileSet;
import org.sonatype.sisu.filetasks.task.WarTask;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ANT based {@link WarTask} implementation.
 *
 * @since 1.0
 */
@Named
class WarTaskImpl
    extends AbstractAntTask<War>
    implements WarTask
{

    private File archive;

    private List<ZipFileSet> libSets = new ArrayList<ZipFileSet>();


    /**
     * Constructor.
     */
    @Inject
    WarTaskImpl()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void prepare( final War war )
    {
        war.setDestFile( checkNotNull( archive ) );

        for (ZipFileSet zipFileSet : libSets) {
            war.addLib(zipFileSet);
        }

        war.setUpdate(true);
        war.setNeedxmlfile(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WarTask setArchive( final File archive )
    {
        this.archive = archive;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Class<? extends War> antTaskType() {
        return War.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WarTask addLibs(File libDir, String libIncludes) {
        ZipFileSet set = new ZipFileSet();
        set.setDir(checkNotNull(libDir));
        set.setIncludes(checkNotNull(libIncludes));
        libSets.add(set);
        return this;
    }

}
