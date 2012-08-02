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
import org.sonatype.sisu.jsw.monitor.internal.log.LogProxy;

/**
 * Talks to the command monitor.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.1
 */
public class CommandMonitorTalker
{

    private final int port;

    private final LogProxy log;

    private static final String host = "127.0.0.1";

    public CommandMonitorTalker( final int port,
                                 final LogProxy log )
    {
        this.port = port;
        this.log = log;
    }

    public void send( final String command )
        throws Exception
    {
        log.info( "Sending {} command to {}:{}", $( command, host, port ) );

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

    private static <T> T[] $( final T... args )
    {
        return args;
    }
}
