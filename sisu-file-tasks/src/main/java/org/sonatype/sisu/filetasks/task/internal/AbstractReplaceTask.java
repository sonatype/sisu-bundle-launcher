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
package org.sonatype.sisu.filetasks.task.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import org.apache.tools.ant.taskdefs.Replace;
import org.sonatype.sisu.filetasks.task.ReplaceSettings;

/**
 * Base class for replace tasks. Prepares common file/directory tasks settings.
 *
 * @since 1.3
 */
abstract class AbstractReplaceTask<ART extends AbstractReplaceTask>
    extends AbstractAntTask<Replace>
    implements ReplaceSettings
{

    /**
     * Replacements. If empty no replacement will take place.
     * Never null.
     */
    private final Map<String, String> replacements;

    /**
     * True if replace should fail when value to be replaced does not exist.
     */
    private boolean failIfReplacedValueDoesNotExist;

    AbstractReplaceTask()
    {
        failIfReplacedValueDoesNotExist = true;
        replacements = new HashMap<String, String>();
    }

    /**
     * Returns a {@link org.apache.tools.ant.taskdefs.Replace} ANT task.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    Class<Replace> antTaskType()
    {
        return Replace.class;
    }

    @Override
    void prepare( final Replace task )
    {
        for ( final Map.Entry<String, String> entry : replacements.entrySet() )
        {
            final Replace.Replacefilter replaceFilter = task.createReplacefilter();
            replaceFilter.setToken( entry.getKey() );
            replaceFilter.setValue( entry.getValue() );
        }
        task.setFailOnNoReplacements( failIfReplacedValueDoesNotExist );
    }

    /**
     * Returns false if file does not exist, true otherwise.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    boolean shouldExecute()
    {
        return replacements.size() > 0;
    }

    @Override
    public ReplaceSettings addReplacement( final String value, final String replacement )
    {
        replacements.put( checkNotNull( value ), checkNotNull( replacement ) );
        return (ART) this;
    }

    @Override
    public ReplaceSettings setFailIfNoReplacementWasMade( final boolean fail )
    {
        failIfReplacedValueDoesNotExist = fail;
        return (ART) this;
    }
}
