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

    private PropertiesTask task;

    private Retargetable file;

    @Inject
    PropertiesBuilderImpl( PropertiesTask task )
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

    public PropertiesBuilderImpl file( final FileRef target )
    {
        this.file.setFileRef( target );
        return this;
    }

    @Override
    public PropertiesBuilderImpl property( final String key, final String value )
    {
        task().setProperty( key, value );
        return this;
    }

    @Override
    public PropertiesBuilderImpl properties( final Properties properties )
    {
        task().setProperties( properties );
        return this;
    }

    @Override
    public PropertiesBuilderImpl properties( final File propertiesFile )
    {
        task().setProperties( propertiesFile );
        return this;
    }

    @Override
    public PropertiesBuilderImpl removeProperty( final String key )
    {
        task().removeProperty( key );
        return this;
    }

    @Override
    public PropertiesBuilderImpl removeAllProperties()
    {
        task().removeAllProperties();
        return this;
    }

    PropertiesTask task()
    {
        return task;
    }

}
