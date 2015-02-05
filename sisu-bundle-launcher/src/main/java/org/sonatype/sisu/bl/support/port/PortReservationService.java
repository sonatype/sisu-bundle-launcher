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

package org.sonatype.sisu.bl.support.port;

import java.util.Set;

import com.google.common.collect.Range;

/**
 * Service that reserves free system ports.
 * <p/>
 * Ports are only guaranteed freely available at port reservation time.
 */
public interface PortReservationService
{

  /**
   * Reserve a port for use
   *
   * @return a free port at time of method call.
   */
  int reservePort();

  /**
   * Cancel the reservation of the specified port, indicating the service shall make it available for future
   * reservations.
   *
   * @param port the port to unreserve
   * @throws IllegalArgumentException if the specified port has not been reserved
   */
  void cancelPort(int port);

  /**
   * Adds a range of ports to be blocked.
   *
   * <p />
   * If the provided range is connected to a previously added range, the span of the ranges forms a new blocked
   * range of ports. If the provided range is not connected to the existing range of blocked ports, it is simply
   * treated as an additional range of ports to be blocked.
   *
   * @param blockedRange a range of ports that are to be blocked from future reservation.
   */
  void addBlockedPorts(Range<Integer> blockedRange);

  /**
   * Adds the set of blocked ports to any existing set of blocked ports.
   *
   * @param blockedSet a set of ports that are to be blocked from future reservation
   */
  void addBlockedPorts(Set<Integer> blockedSet);

  /**
   * Adds blocked ports to any existing set of blocked ports.
   *
   * @param ports ports that are to be blocked from future reservation
   * @since 1.9
   */
  void addBlockedPorts(int... ports);
}
