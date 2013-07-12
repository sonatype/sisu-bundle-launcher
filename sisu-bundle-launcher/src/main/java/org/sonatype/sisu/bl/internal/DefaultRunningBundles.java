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
package org.sonatype.sisu.bl.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.support.RunningBundles;
import org.sonatype.sisu.goodies.common.ComponentSupport;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

/**
 * Keeps a list of current running bundles.
 *
 * @since 1.0
 */
@Named
@Singleton
public class DefaultRunningBundles
    extends ComponentSupport
    implements RunningBundles
{

    /**
     * List of running bundles.
     */
    private final Set<Bundle> bundles;

    @Inject
    public DefaultRunningBundles()
    {
        bundles = Collections.synchronizedSet( Sets.<Bundle>newHashSet() );
    }

    @Override
    public void add( final Bundle bundle )
    {
        bundles.add( checkNotNull( bundle ) );
        log.debug( "Added bundle: {}", bundle );
    }

    @Override
    public void remove( final Bundle bundle )
    {
        bundles.remove( checkNotNull( bundle ) );
        log.debug( "Removed bundle: {}", bundle );
    }

    @Override
    public Bundle[] get()
    {
        return bundles.toArray( new Bundle[bundles.size()] );
    }

    @Override
    public Bundle[] get( final Class<?> bundleType )
    {
        checkNotNull( bundleType );
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
