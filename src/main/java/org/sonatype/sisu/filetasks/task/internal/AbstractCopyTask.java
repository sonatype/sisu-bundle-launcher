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

import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FilterSet;
import org.sonatype.sisu.filetasks.task.CopySettings;

import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Base class for copy tasks. Prepares common file/directory tasks settings.
 *
 * @since 1.0
 */
abstract class AbstractCopyTask<AFCT extends AbstractCopyTask>
    extends AbstractAntTask<Copy>
    implements CopySettings
{

    /**
     * If a file which is newer in destination should be replaced. Default true.
     */
    private boolean overwriteNewer;

    /**
     * If a file which is read only in destination should be replaced. Default true.
     */
    private boolean overwriteReadOnly;

    /**
     * Filtering properties. If empty no filtering will take place.
     * Never null.
     */
    private final Map<String, String> properties;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    AbstractCopyTask()
    {
        overwriteNewer = true;
        overwriteReadOnly = true;
        properties = new HashMap<String, String>();
    }

    /**
     * Returns a {@link Copy} ANT task.
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Class<Copy> antTaskType()
    {
        return Copy.class;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    void prepare( final Copy copy )
    {
        copy.setOverwrite( overwriteNewer );

        copy.setForce( overwriteReadOnly );

        if ( properties.size() > 0 )
        {
            FilterSet globalFilterSet = copy.getProject().getGlobalFilterSet();
            globalFilterSet.setBeginToken( "${" );
            globalFilterSet.setEndToken( "}" );
            for ( Map.Entry<String, String> entry : properties.entrySet() )
            {
                globalFilterSet.addFilter( entry.getKey(), entry.getValue() );
            }
            copy.setFiltering( true );
        }

        copy.setFailOnError( true );
    }

    @Override
    public AFCT setOverwriteNewer( final boolean overwrite )
    {
        this.overwriteNewer = overwrite;
        return (AFCT) this;
    }

    @Override
    public AFCT setOverwriteReadOnly( final boolean overwrite )
    {
        this.overwriteReadOnly = overwrite;
        return (AFCT) this;
    }

    @Override
    public AFCT addFilter( final String key, final String value )
    {
        this.properties.put( key, value );
        return (AFCT) this;
    }

    @Override
    public AFCT addFilters( final Properties properties )
    {
        for ( Map.Entry<Object, Object> entry : properties.entrySet() )
        {
            addFilter( (String) entry.getKey(), (String) entry.getValue() );
        }
        return (AFCT) this;
    }

    @Override
    public AFCT addFilters( final File propertiesFile )
    {
        addFilters( PropertiesHelper.load( propertiesFile ) );
        return (AFCT) this;
    }

}
