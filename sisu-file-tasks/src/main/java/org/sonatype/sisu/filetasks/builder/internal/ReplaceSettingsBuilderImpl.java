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

import org.sonatype.sisu.filetasks.builder.ReplaceSettingsBuilder;
import org.sonatype.sisu.filetasks.task.ReplaceSettings;

/**
 * {@link ReplaceSettingsBuilder} implementation.
 *
 * @since 1.3
 */
abstract class ReplaceSettingsBuilderImpl<RSB extends ReplaceSettingsBuilder, RS extends ReplaceSettings>
    extends BuilderImpl
    implements ReplaceSettingsBuilder<RSB>
{

    /**
     * Task to be used.
     * Never null.
     */
    private RS task;

    ReplaceSettingsBuilderImpl( final RS task )
    {
        this.task = task;
    }

    @Override
    public RSB doNotFailIfNoReplacementWasMade()
    {
        task().setFailIfNoReplacementWasMade( false );
        return (RSB) this;
    }

    @Override
    public RSB failIfNoReplacementWasMade()
    {
        task().setFailIfNoReplacementWasMade( true );
        return (RSB) this;
    }

    @Override
    public RSB replace( final String value, final String replacement )
    {
        task().addReplacement( value, replacement );
        return (RSB) this;
    }

    RS task()
    {
        return task;
    }

}
