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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.sisu.bl.support.port.PortReservationService;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Act as a registry of reservedPorts available to use in the JVM.
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
public class DefaultPortReservationService
    implements PortReservationService
{
  private static final int MAX_ATTEMPTS = 10;

  private final List<Range> blockedPortRanges;

  private final Set<Integer> blockedPorts;

  private final Set<Integer> reservedPorts;

  @Inject
  public DefaultPortReservationService() {
    this(null, null);
  }

  /**
   * @param blockedSet a disparate set of reservedPorts to be blocked from reservation
   */
  public DefaultPortReservationService(Set<Integer> blockedSet) {
    this(null, blockedSet);
  }

  /**
   * @param blockedRange a contiguous range of reservedPorts to be blocked from reservation
   */
  public DefaultPortReservationService(Range<Integer> blockedRange) {
    this(blockedRange, null);
  }

  private DefaultPortReservationService(Range<Integer> blockedRange, Set<Integer> blockedSet) {
    this.blockedPortRanges = Lists.newArrayList();
    this.blockedPorts = Sets.newHashSet();
    this.reservedPorts = Sets.newHashSet();
    if (blockedRange != null) {
      addBlockedPorts(blockedRange);
    }
    if (blockedSet != null) {
      addBlockedPorts(blockedSet);
    }
  }

  /**
   * @return a port that was 'free' and not explicitly blocked at the time of call
   * @throws RuntimeException if a port could not be reserved
   */
  @Override
  public synchronized int reservePort() {
    int port = 0;
    int attempts = 0;
    boolean searchingForPort = true;
    while (searchingForPort && ++attempts < MAX_ATTEMPTS) {
      port = findFreePort();
      searchingForPort = isBlocked(port) || !reservedPorts.add(port);
    }
    if (searchingForPort) {
      throw new RuntimeException("Could not allocate a free port after " + MAX_ATTEMPTS + " attempts.");
    }
    return port;
  }

  @Override
  public synchronized void cancelPort(int port) {
    checkNotNull(port);
    if (!reservedPorts.remove(port)) {
      throw new IllegalArgumentException("port " + port + " not yet reserved by this service.");
    }
  }

  @Override
  public synchronized void addBlockedPorts(Range<Integer> blockedRange) {
    checkNotNull(blockedRange);
    this.blockedPortRanges.add(blockedRange);
  }

  @Override
  public synchronized void addBlockedPorts(Set<Integer> blockedSet) {
    checkNotNull(blockedSet);
    this.blockedPorts.addAll(blockedSet);
  }

  @Override
  public void addBlockedPorts(final int... ports) {
    this.blockedPorts.addAll(Ints.asList(ports));
  }

  private boolean isBlocked(int port) {
    for (Range r : this.blockedPortRanges) {
      if (r.contains(port)) {
        return true;
      }
    }
    return blockedPorts.contains(port);
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
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }

    Integer portNumber = server.getLocalPort();
    try {
      server.close();
    }
    catch (IOException e) {
      throw new RuntimeException("Unable to release port " + portNumber, e);
    }
    return portNumber;
  }
}
