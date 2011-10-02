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

    public static final String LOG_TO_SYSTEM_OUT = CommandMonitorTalker.class.getName() + ".logToSystemOut";

    public static final String STOP_COMMAND = "STOP";

    public static final String RESTART_COMMAND = "RESTART";

    private final ServerSocket socket;

    private final boolean logToSystemOut;

    public CommandMonitorThread( final int port )
        throws IOException
    {
        setDaemon( true );
        setName( "Bootstrap Command Monitor" );
        // Only listen on local interface
        this.socket = new ServerSocket( port, 1, InetAddress.getByName( "127.0.0.1" ) );

        this.logToSystemOut = Boolean.getBoolean( LOG_TO_SYSTEM_OUT );
    }

    @Override
    public void run()
    {
        debug( "Listening for commands: {}", socket );

        boolean running = true;
        while ( running )
        {
            try
            {
                Socket client = socket.accept();
                debug( "Accepted client: {}", client );

                BufferedReader reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
                String command = reader.readLine();
                debug( "Read command: {}", command );
                client.close();

                if ( STOP_COMMAND.equals( command ) )
                {
                    debug( "Stopping" );
                    WrapperManager.stopAndReturn( 0 );
                    running = false;
                }
                else if ( RESTART_COMMAND.equals( command ) )
                {
                    debug( "Restarting" );
                    WrapperManager.restartAndReturn();
                }
                else
                {
                    error( "Unknown command: {}", command );
                }

                socket.close();
            }
            catch ( Exception e )
            {
                error( "Failed", e );
            }
        }

        debug( "Done" );
    }

    protected void debug( final String message, Object... args )
    {
        if ( logToSystemOut )
        {
            System.out.println( "CommandMonitorThread: " + String.format( message.replace( "{}", "%s" ), args ) );
        }
    }

    protected void error( final String message, Object... args )
    {
        debug( message, args );
    }

}
