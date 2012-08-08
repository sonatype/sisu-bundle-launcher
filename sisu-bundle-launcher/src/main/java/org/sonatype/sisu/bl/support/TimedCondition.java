/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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
     * @param delay    before first check of condition (milliseconds)
     * @param timeout  after which condition is considered unsatisfied (milliseconds)
     * @param interval between checks that condition is satisfied (milliseconds)
     * @return true if condition was satisfied before the specified timeout
     */
    public boolean await( final long delay, final long timeout, final long interval )
    {
        sleep( delay );
        long start = System.currentTimeMillis();
        while ( System.currentTimeMillis() < start + timeout )
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
            sleep( Math.min( timeout, interval ) );
        }
        return false;
    }

    /**
     * Waits for this condition to become satisfied without a delay.
     *
     * @param timeout  after which condition is considered unsatisfied (milliseconds)
     * @param interval between checks that condition is satisfied (milliseconds)
     * @return true if condition was satisfied before the specified timeout
     */
    public boolean await( final long timeout, final long interval )
    {
        return await( 0, timeout, interval );
    }

    /**
     * Waits for this condition to become satisfied without a delay, checking the condition at each second.
     *
     * @param timeout after which condition is considered unsatisfied (milliseconds)
     * @return true if condition was satisfied before the specified timeout
     */
    public boolean await( final long timeout )
    {
        return await( 0, timeout, 1000 );
    }

    /**
     * Sleep for specified number of milliseconds.
     *
     * @param delay number of milliseconds to sleep
     */
    protected void sleep( final long delay )
    {
        if ( delay > 0 )
        {
            try
            {
                Thread.sleep( delay );
            }
            catch ( InterruptedException ignore )
            {
                // ignore
            }
        }
    }

}
