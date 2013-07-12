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
package org.sonatype.sisu.bl.support;

import static com.google.common.base.Preconditions.checkNotNull;

import org.sonatype.sisu.goodies.common.Time;

/**
 * Waits for a condition to become satisfied in a timed manner.
 *
 * @since 1.3
 */
public abstract class TimedCondition
{

    /**
     * When the condition becomes satisfied.
     *
     * @return true if condition is satisfied
     * @throws Exception if evaluating the condition failed. It will be ignored (and considered that condition was not
     *                   satisfied)
     */
    protected abstract boolean isSatisfied()
        throws Exception;

    /**
     * Waits for this condition to become satisfied.
     *
     * @param delay    before first check of condition
     * @param timeout  after which condition is considered unsatisfied
     * @param interval between checks that condition is satisfied
     * @return true if condition was satisfied before the specified timeout
     */
    public boolean await( final Time delay, final Time timeout, final Time interval )
    {
        checkNotNull( delay );
        checkNotNull( timeout );
        checkNotNull( interval );

        sleep( delay );
        long start = System.currentTimeMillis();
        while ( System.currentTimeMillis() < start + timeout.toMillis() )
        {
            try
            {
                if ( isSatisfied() )
                {
                    return true;
                }
            }
            catch ( final Exception e )
            {
                // ignore
            }
            sleep( timeout.toMillis() < interval.toMillis() ? timeout : interval );
        }
        return false;
    }

    /**
     * Waits for this condition to become satisfied without a delay.
     *
     * @param timeout  after which condition is considered unsatisfied
     * @param interval between checks that condition is satisfied
     * @return true if condition was satisfied before the specified timeout
     */
    public boolean await( final Time timeout, final Time interval )
    {
        return await( Time.millis( 0 ), timeout, interval );
    }

    /**
     * Waits for this condition to become satisfied without a delay, checking the condition at each second.
     *
     * @param timeout after which condition is considered unsatisfied
     * @return true if condition was satisfied before the specified timeout
     */
    public boolean await( final Time timeout )
    {
        return await( Time.millis( 0 ), timeout, Time.seconds( 1 ) );
    }

    /**
     * Sleep for specified time.
     *
     * @param delay time to sleep
     */
    protected void sleep( final Time delay )
    {
        if ( delay.toMillis() > 0 )
        {
            try
            {
                Thread.sleep( delay.toMillis() );
            }
            catch ( InterruptedException ignore )
            {
                // ignore
            }
        }
    }

}
