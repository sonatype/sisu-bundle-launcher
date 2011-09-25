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

import org.sonatype.sisu.filetasks.support.PropertiesHelper;
import org.sonatype.sisu.filetasks.task.PropertiesTask;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.sisu.filetasks.support.PropertiesHelper.load;
import static org.sonatype.sisu.filetasks.support.PropertiesHelper.save;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
class PropertiesTaskImpl
    implements PropertiesTask
{

    private File propertiesFile;

    private Map<String, String> propertiesToSet;

    private List<String> propertiesToRemove;

    private boolean removeAllProperties;

    @Inject
    PropertiesTaskImpl()
    {
        propertiesToSet = new HashMap<String, String>();
        propertiesToRemove = new ArrayList<String>();
        removeAllProperties = false;
    }

    @Override
    public void run()
    {
        Properties properties = load( checkNotNull( propertiesFile ) );

        if ( removeAllProperties )
        {
            properties.clear();
        }

        if ( propertiesToRemove.size() > 0 )
        {
            for ( String key : propertiesToRemove )
            {
                properties.remove( key );
            }
        }

        if ( propertiesToSet.size() > 0 )
        {
            for ( Map.Entry<String, String> entry : propertiesToSet.entrySet() )
            {
                properties.setProperty( entry.getKey(), entry.getValue() );
            }
        }

        save( properties, propertiesFile );
    }

    @Override
    public PropertiesTask setTargetFile( final File propertiesFile )
    {
        this.propertiesFile = propertiesFile;
        return this;
    }

    @Override
    public PropertiesTask setProperty( final String key, final String value )
    {
        propertiesToSet.put( key, value );
        return this;
    }

    @Override
    public PropertiesTask setProperties( final Properties properties )
    {
        for ( Map.Entry<Object, Object> entry : properties.entrySet() )
        {
            setProperty( (String) entry.getKey(), (String) entry.getValue() );
        }
        return this;
    }

    @Override
    public PropertiesTask setProperties( final File propertiesFile )
    {
        return setProperties( PropertiesHelper.load( propertiesFile ) );
    }

    @Override
    public PropertiesTask removeProperty( final String key )
    {
        return this;
    }

    @Override
    public PropertiesTask removeAllProperties()
    {
        return this;
    }

}
