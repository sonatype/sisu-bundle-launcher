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

package org.sonatype.sisu.bl.internal;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.support.RunningBundles;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps a list of current running bundles.
 *
 * @since 1.0
 */
@Named
public class DefaultRunningBundles
    implements RunningBundles
{

    /**
     * List of running bundles.
     */
    private Set<Bundle> bundles;

    @Inject
    public DefaultRunningBundles()
    {
        bundles = Collections.synchronizedSet( new HashSet<Bundle>() );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public void add( final Bundle bundle )
    {
        bundles.add( bundle );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public void remove( final Bundle bundle )
    {
        bundles.remove( bundle );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public Bundle[] get()
    {
        return bundles.toArray( new Bundle[bundles.size()] );
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public Bundle[] get( final Class<?> bundleType )
    {
        if ( bundleType==null ) {
             return get();
        }
        else {
            final Collection<Bundle> filtered = Collections2.filter( bundles, new Predicate<Bundle>()
            {
                @Override
                public boolean apply( final Bundle input )
                {
                    return bundleType.isInstance( input );
                }

            } );
            return filtered.toArray( new Bundle[filtered.size()] );
        }
    }

}
