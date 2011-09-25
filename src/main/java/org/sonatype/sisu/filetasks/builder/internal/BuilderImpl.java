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

import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.Targetable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO
 *
 * @since 1.0
 */
public abstract class BuilderImpl
    implements FileTask, Targetable
{

    private Collection<Retargetable> retargetables;

    BuilderImpl()
    {
        retargetables = new ArrayList<Retargetable>();
    }

    Retargetable addRetargetable( Retargetable retargetable )
    {
        retargetables.add( retargetable );
        return retargetable;
    }

    @Override
    public void run()
    {
        task().run();
    }

    @Override
    public void setTargetDirectory( final File targetDirectory )
    {
        for ( Retargetable retargetable : retargetables )
        {
            if ( retargetable.fileRef != null )
            {
                retargetable.retargetAs( retargetable.fileRef.retarget( targetDirectory ).getFile() );
            }
        }
    }

    abstract FileTask task();

    static abstract class Retargetable
    {

        private FileRef fileRef;

        void setFileRef( FileRef fileRef )
        {
            this.fileRef = fileRef;
            retargetAs( this.fileRef.getFile() );
        }

        abstract void retargetAs( File file );

    }

}
