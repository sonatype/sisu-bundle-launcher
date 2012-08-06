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
 * @since 1.0
 */
public abstract class TimedCondition
{

    protected abstract boolean isSatisfied() throws Exception;

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

    public boolean await( final long timeout, final long interval )
    {
        return await( 0, timeout, interval );
    }

    public boolean await( final long timeout )
    {
        return await( 0, timeout, 1000 );
    }

}
