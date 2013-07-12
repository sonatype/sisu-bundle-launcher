/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import org.sonatype.sisu.bl.support.port.PortReservationService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Act as a registry of ports available to use in the JVM.
 *
 * Ports which are 'free' and not explicitly blocked may be reserved for use.
 *
 * A free port is a port that this service can open a server socket with at the time of reservation.
 * A blocked port is either explicitly blocked or previously reserved.
 *
 * @author plynch
 */
@Singleton
@Named
public class DefaultPortReservationService implements PortReservationService {


    private Range<Integer> blockedPortRange;
    private List<Range> blockedPortRanges;
    private Set<Integer> blockedPortSet;

    @Inject
    public DefaultPortReservationService() {
        this(null,null);
    }

    /**
     *
     * @param blockedSet a disparate set of ports to be blocked from reservation
     */
    public DefaultPortReservationService(Set<Integer> blockedSet) {
        this(null, blockedSet);
    }

    /**
     *
     * @param blockedRange a contiguous range of ports to be blocked from reservation
     */
    public DefaultPortReservationService(Range<Integer> blockedRange) {
        this(blockedRange, null);
    }

    /**
     *
     * @param blockedRange
     * @param blockedSet
     */
    private DefaultPortReservationService(Range<Integer> blockedRange, Set<Integer> blockedSet) {
        this.blockedPortRange = blockedRange;
        this.blockedPortRanges = Lists.newArrayList();
        this.blockedPortSet = blockedSet;
    }

    /**
     * Port registry
     */
    private final Set<Integer> ports = Sets.newHashSet();


    private boolean isBlocked(int port){
        for (Range r : this.blockedPortRanges){
            if(r.contains(port)){
                return true;
            }
        }
        return (this.blockedPortRange != null && this.blockedPortRange.contains(port))
            || (this.blockedPortSet != null && this.blockedPortSet.contains(port));
    }

    /**
     *
     * @return a port that was 'free' and not explicitly blocked at the time of call
     * @throws RuntimeException if a port could not be reserved
     */
    @Override
    public int reservePort() {
        int port = 0;
        int attempts = 0;
        boolean searchingForPort = true;
        synchronized (ports) {
            while (searchingForPort && ++attempts < 10) {
                port = findFreePort();
                searchingForPort = !ports.add(port) || isBlocked(port);
            }
        }
        if (!(attempts < 10)) {
            throw new RuntimeException("Could not allocate a free port after " + attempts + " attempts.");
        }
        return port;
    }

    @Override
    public void cancelPort(int port) {
        checkNotNull(port);
        synchronized (ports) {
            if (!ports.remove(port)) {
                throw new IllegalArgumentException("port " + port + " not yet reserved by this service.");
            }
        }
    }

    @Override
    public void addBlockedPorts(Range<Integer> blockedRange) {
        checkNotNull(blockedRange);
        synchronized (ports){
            if(this.blockedPortRange == null){
                this.blockedPortRange = blockedRange;
            } else if (this.blockedPortRange.isConnected(blockedRange)){
                this.blockedPortRange = this.blockedPortRange.span(blockedRange);
            } else {
                this.blockedPortRanges.add(blockedRange);
            }
        }
    }

    @Override
    public void addBlockedPorts(Set<Integer> blockedSet) {
        checkNotNull(blockedSet);
        synchronized (ports){
            if(this.blockedPortSet == null){
                this.blockedPortSet = blockedSet;
            } else {
                this.blockedPortSet.addAll(blockedSet);
            }
        }
    }

    /**
     * Find a random free system port.
     *
     * @return a free system port at the time this method was called.
     */
    @VisibleForTesting
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
