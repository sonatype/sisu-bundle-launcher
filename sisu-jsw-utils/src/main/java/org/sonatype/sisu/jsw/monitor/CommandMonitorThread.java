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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.sonatype.sisu.jsw.monitor.internal.log.LogProxy;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 * Thread which listens for command messages to control the JVM.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class CommandMonitorThread
    extends Thread
{

    public static final String STOP_COMMAND = "STOP";

    public static final String RESTART_COMMAND = "RESTART";

    private final ServerSocket socket;

    private LogProxy log;

    public CommandMonitorThread( final int port,
                                 final LogProxy log )
        throws IOException
    {
        this.log = log;
        this.socket = new ServerSocket( port, 1, InetAddress.getByName( "127.0.0.1" ) );

        setDaemon( true );
        setName( "Bootstrap Command Monitor" );
    }

    @Override
    public void run()
    {
        log.debug( "Listening for commands: {}", socket );

        boolean running = true;
        while ( running )
        {
            try
            {
                Socket client = socket.accept();
                log.debug( "Accepted client: {}", client );

                BufferedReader reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
                String command = reader.readLine();
                log.debug( "Read command: {}", command );
                client.close();

                if ( STOP_COMMAND.equals( command ) )
                {
                    log.debug( "Stopping JSW" );
                    WrapperManager.stop( 0 );
                    running = false;
                }
                else if ( RESTART_COMMAND.equals( command ) )
                {
                    log.debug( "Restarting JSW" );
                    WrapperManager.restartAndReturn();
                }
                else
                {
                    log.error( "Unknown command: {}", command );
                }

                socket.close();
            }
            catch ( Exception e )
            {
                log.error( "Failed", e );
            }
        }

        log.debug( "Done" );
    }

}
