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

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import org.sonatype.sisu.jsw.monitor.internal.log.LogProxy;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 * Thread which listens for command messages to control the JVM.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ShutdownIfNotAliveThread
    extends Thread
{

    static final String LOCALHOST = "127.0.0.1";

    private int port;

    private int pingInterval;

    private int timeout;

    private LogProxy log;

    private boolean running;

    private Socket socket;

    private final CommandMonitorTalker talker;

    public ShutdownIfNotAliveThread( final int port,
                                     final int pingInterval,
                                     final int timeout,
                                     final LogProxy log )
        throws IOException
    {
        this.port = port;
        this.pingInterval = pingInterval;
        this.timeout = timeout;
        this.log = log;

        talker = new CommandMonitorTalker( port, log );

        this.running = true;
        this.setDaemon( true );
        setName( "Shutdown if not alive" );
    }

    @Override
    public void run()
    {
        log.info( "Shutdown thread pinging on port {} every {} milliseconds", port, pingInterval );

        while ( running )
        {
            try
            {
                ping();
                sleep( pingInterval );
            }
            catch ( InterruptedException ignore )
            {
                ping();
            }
        }

        log.debug( "Done" );
    }

    private void ping()
    {
        try
        {
            log.debug( "Pinging on port {} ...", port );

            talker.send( "PING" );
        }
        catch ( ConnectException e )
        {
            log.info( "Exception got while pinging {}:{}", e.getClass().getName(), e.getMessage() );

            running = false;
            shutdownJSW();
        }
        catch ( Exception e )
        {
            log.info( "Skipping exception got while pinging {}:{}", e.getClass().getName(), e.getMessage() );
        }
        finally
        {
            close( socket );
        }
    }

    void shutdownJSW()
    {
        log.info( "Shutting down JSW as there is no ping response on port {}", port );
        WrapperManager.stop( 0 );
    }

    public void stopRunning()
    {
        running = false;
        close( socket );
    }

    private void close( final Socket socket )
    {
        if ( socket != null )
        {
            try
            {
                socket.close();
            }
            catch ( IOException ignore )
            {
                // ignore
            }
        }
    }

}
