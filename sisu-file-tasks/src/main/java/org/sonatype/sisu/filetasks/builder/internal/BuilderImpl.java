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
package org.sonatype.sisu.filetasks.builder.internal;

import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.Targetable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for builders.
 *
 * @since 1.0
 */
public abstract class BuilderImpl
    implements FileTask, Targetable
{

    /**
     * List of re-target-able paths.
     */
    private Collection<Retargetable> retargetables;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    BuilderImpl()
    {
        retargetables = new ArrayList<Retargetable>();
    }

    /**
     * Adds a re-targetable path.
     *
     * @param retargetable to add
     * @return passed retargetable for fluent API usage
     */
    Retargetable addRetargetable( final Retargetable retargetable )
    {
        retargetables.add( retargetable );
        return retargetable;
    }

    /**
     * Executes builder created task.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public final void run()
    {
        task().run();
    }

    /**
     * Re-targets all re-target-ables to specified directory.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
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

    /**
     * Returns the builder specific task to be run.
     *
     * @return builder specific task to be run
     */
    abstract FileTask task();

    /**
     * A re-target-able file reference callback.
     *
     * @since 1.0
     */
    static abstract class Retargetable
    {

        /**
         * Re-target-able file reference.
         */
        private FileRef fileRef;

        /**
         * File reference setter.
         *
         * @param fileRef to set
         */
        void setFileRef( FileRef fileRef )
        {
            this.fileRef = fileRef;
            retargetAs( this.fileRef.getFile() );
        }

        /**
         * Callback when a reference is re-targeted.
         *
         * @param file the new referenced file (after re-targeting took place)
         */
        abstract void retargetAs( File file );

    }

}
