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

import java.lang.reflect.Method;

import org.sonatype.sisu.jsw.monitor.internal.log.LogProxy;
import org.sonatype.sisu.jsw.monitor.internal.log.SystemOutLogProxy;

/**
 * TODO
 *
 * @since 1.0
 */
public class Launcher
{

    public static final String LAUNCHER = Launcher.class.getName() + ".launcher";

    public static final String MONITOR_PORT = Launcher.class.getName() + ".monitor.port";

    public static final String KEEP_ALIVE_PORT = Launcher.class.getName() + ".keepAlive.port";

    public static final String KEEP_ALIVE_PING_INTERVAL = Launcher.class.getName() + ".keepAlive.pingInterval";

    public static final String KEEP_ALIVE_TIMEOUT = Launcher.class.getName() + ".keepAlive.timeout";

    public static final String LOG_TO_SYSTEM_OUT = Launcher.class.getName() + ".logToSystemOut";

    private static final int FIVE_SECONDS = 5000;

    private static final int ONE_SECOND = 1000;

    public static void main( String[] args )
        throws Exception
    {
        // find wrapped launcher
        String launcher = System.getProperty( LAUNCHER );
        if ( launcher == null )
        {
            throw new IllegalStateException( "Launcher must be specified via system property: " + LAUNCHER );
        }

        Class<?> launcherClass = Launcher.class.getClassLoader().loadClass( launcher );
        Method main = launcherClass.getMethod( "main", args.getClass() );

        // Setup command monitor if configured
        Integer commandMonitorPort = Integer.getInteger( MONITOR_PORT );
        if ( commandMonitorPort != null )
        {
            LogProxy logProxy = Boolean.getBoolean( LOG_TO_SYSTEM_OUT )
                ? new SystemOutLogProxy( CommandMonitorThread.class )
                : new LogProxy();
            new CommandMonitorThread( commandMonitorPort, logProxy ).start();
        }

        // Setup keep alive if configured
        Integer keepAlivePort = Integer.getInteger( KEEP_ALIVE_PORT );
        if ( keepAlivePort != null )
        {
            LogProxy logProxy = Boolean.getBoolean( LOG_TO_SYSTEM_OUT )
                ? new SystemOutLogProxy( ShutdownIfNotAliveThread.class )
                : new LogProxy();
            new ShutdownIfNotAliveThread( keepAlivePort,
                                          Integer.getInteger( KEEP_ALIVE_PING_INTERVAL, FIVE_SECONDS ),
                                          Integer.getInteger( KEEP_ALIVE_TIMEOUT, ONE_SECOND ),
                                          logProxy
            ).start();
        }

        main.invoke( null, new Object[]{ args } );
    }

}
