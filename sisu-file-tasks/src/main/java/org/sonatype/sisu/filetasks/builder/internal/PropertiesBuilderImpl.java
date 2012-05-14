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
import org.sonatype.sisu.filetasks.builder.PropertiesBuilder;
import org.sonatype.sisu.filetasks.task.PropertiesTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Properties;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
public class PropertiesBuilderImpl
    extends BuilderImpl
    implements PropertiesBuilder
{

    /**
     * Task to be used.
     */
    private PropertiesTask task;

    /**
     * Re-target-able properties files to be created/updated.
     */
    private Retargetable file;

    /**
     * Constructor.
     *
     * @param task {@link PropertiesTask} to be used
     * @since 1.0
     */
    @Inject
    PropertiesBuilderImpl( final PropertiesTask task )
    {
        this.task = task;
        file = addRetargetable( new Retargetable()
        {
            @Override
            void retargetAs( final File file )
            {
                task().setTargetFile( file );
            }
        } );
    }

    /**
     * Properties file to be modified. If file does not exist it will be created.
     *
     * @param propertiesFile to be created/modified
     * @return itself, for fluent API usage
     * @since 1.0
     */
    public PropertiesBuilderImpl file( final FileRef propertiesFile )
    {
        this.file.setFileRef( propertiesFile );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public PropertiesBuilderImpl property( final String key, final String value )
    {
        task().setProperty( key, value );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public PropertiesBuilderImpl properties( final Properties properties )
    {
        task().setProperties( properties );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public PropertiesBuilderImpl properties( final File propertiesFile )
    {
        task().setProperties( propertiesFile );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public PropertiesBuilderImpl removeProperty( final String key )
    {
        task().removeProperty( key );
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public PropertiesBuilderImpl removeAllProperties()
    {
        task().removeAllProperties();
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    PropertiesTask task()
    {
        return task;
    }

}
