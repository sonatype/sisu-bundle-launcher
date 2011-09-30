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

import org.sonatype.sisu.filetasks.builder.CopySettingsBuilder;
import org.sonatype.sisu.filetasks.task.CopySettings;

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
class CopySettingsBuilderImpl<BI extends CopySettingsBuilder, CS extends CopySettings>
    extends BuilderImpl
    implements CopySettingsBuilder<BI>
{

    /**
     * Task to be used.
     */
    private CS task;

    @Inject
    CopySettingsBuilderImpl( final CS task )
    {
        this.task = task;
    }

    @Override
    public BI overwriteNewer()
    {
        task().setOverwriteNewer( true );
        return (BI) this;
    }

    @Override
    public BI doNotOverwriteNewer()
    {
        task().setOverwriteNewer( false );
        return (BI) this;
    }

    @Override
    public BI overwriteReadOnly()
    {
        task().setOverwriteReadOnly( true );
        return (BI) this;
    }

    @Override
    public BI doNotOverwriteReadOnly()
    {
        task().setOverwriteReadOnly( false );
        return (BI) this;
    }

    @Override
    public BI filterUsing( final String key, final String value )
    {
        task().addFilter( key, value );
        return (BI) this;
    }

    @Override
    public BI filterUsing( final Properties properties )
    {
        task().addFilters( properties );
        return (BI) this;
    }

    @Override
    public BI filterUsing( final File propertiesFile )
    {
        task().addFilters( propertiesFile );
        return (BI) this;
    }

    @Override
    public BI failIfSourceDoesNotExist()
    {
        task().setFailIfSourceDoesNotExist( true );
        return (BI) this;
    }

    @Override
    public BI doNotFailIfSourceDoesNotExist()
    {
        task().setFailIfSourceDoesNotExist( false );
        return (BI) this;
    }

    CS task()
    {
        return task;
    }

}
