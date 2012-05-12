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
package org.sonatype.sisu.bl.support.port.internal;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import org.sonatype.sisu.bl.support.port.PortReservationService;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;

/**
 * Act as a registry of ports available to use in the JVM.
 *
 * @author plynch
 */
@Singleton
@Named
public class DefaultPortReservationService implements PortReservationService {

    /**
     * Port registry
     */
    private final Set<Integer> ports = Sets.newHashSet();

    @Override
    public int reservePort() {
        int port = 0;
        int attempts = 0;
        boolean searchingForPort = true;
        synchronized (ports) {
            while (searchingForPort && ++attempts < 10) {
                port = findFreePort();
                searchingForPort = !ports.add(port);
            }
        }
        if (!(attempts < 10)) {
            throw new RuntimeException("Could not allocate a free port after " + attempts + " attempts.");
        }
        return port;
    }

    @Override
    public void cancelPort(int port) {
        Preconditions.checkNotNull(port);
        synchronized (ports) {
            if (!ports.remove(port)) {
                throw new IllegalArgumentException("port " + port + " not yet reserved by this service.");
            }
        }
    }

    /**
     * Find a random free system port.
     *
     * @return a free system port at the time this method was called.
     */
    protected int findFreePort() {
        ServerSocket server;
        try {
            server = new ServerSocket(0);
        } catch (IOException e) {
            throw Throwables.propagate( e );
        }

        Integer portNumber = server.getLocalPort();
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to release port " + portNumber, e);
        }
        return portNumber;
    }
}
