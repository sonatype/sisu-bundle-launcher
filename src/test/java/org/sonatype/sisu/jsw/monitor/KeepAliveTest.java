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

package org.sonatype.sisu.jsw.monitor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.sonatype.sisu.jsw.monitor.internal.log.Slf4jLogProxy;
import org.sonatype.sisu.litmus.testsupport.TestSupport;

/**
 * TODO
 *
 * @since 1.0
 */
public class KeepAliveTest
    extends TestSupport
{

    @Test
    public void keepAlive()
        throws Exception
    {
        Slf4jLogProxy katLog = new Slf4jLogProxy( LoggerFactory.getLogger( KeepAliveThread.class ) );
        KeepAliveThread keepAliveThread = new KeepAliveThread( 0, katLog );
        keepAliveThread.start();

        final AtomicBoolean shutDown = new AtomicBoolean( false );

        Slf4jLogProxy aliveLog = new Slf4jLogProxy( LoggerFactory.getLogger( ShutdownIfNotAliveThread.class ) );
        ShutdownIfNotAliveThread aliveThread =
            new ShutdownIfNotAliveThread( keepAliveThread.getPort(), 100, 1000, aliveLog )
            {
                @Override
                void shutdownJSW()
                {
                    shutDown.set( true );
                }
            };
        aliveThread.start();

        Thread.sleep( 2000 );

        keepAliveThread.stopRunning();
        keepAliveThread.join();

        aliveThread.interrupt();
        aliveThread.stopRunning();
        aliveThread.join();

        assertThat( shutDown.get(), is( true ) );
    }

}
