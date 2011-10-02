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

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Talks to the command monitor.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.1
 */
public class CommandMonitorTalker
{

    private static Logger log = LoggerFactory.getLogger( CommandMonitorTalker.class );

    private final int port;

    private static final String host = "127.0.0.1";

    public CommandMonitorTalker( final int port )
    {
        this.port = port;
    }

    public void send( final String command )
    {
        log.info( "Sending {} command to {}:{}", $( command, host, port ) );
        try
        {
            Socket socket = new Socket();
            socket.setSoTimeout( 5000 );
            socket.connect( new InetSocketAddress( host, port ) );
            try
            {
                OutputStream output = socket.getOutputStream();
                output.write( command.getBytes() );
                output.close();
            }
            finally
            {
                socket.close();
            }
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public static void installStopShutdownHook( final int commandPort )
    {
        Thread stopShutdownHook = new Thread( "JSW Sanity Stopper" )
        {
            @Override
            public void run()
            {
                log.info( "Sending server stop command" );
                try
                {
                    new CommandMonitorTalker( commandPort ).send( CommandMonitorThread.STOP_COMMAND );
                }
                catch ( Exception e )
                {
                    // ignore
                }
            }
        };

        Runtime.getRuntime().addShutdownHook( stopShutdownHook );
        log.info( "Installed stop shutdown hook" );
    }

    private static <T> T[] $( final T... args )
    {
        return args;
    }
}
