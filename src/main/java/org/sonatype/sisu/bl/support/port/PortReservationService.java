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
package org.sonatype.sisu.bl.support.port;

/**
 * Service that reserves free system ports.
 * <p/>
 * Ports are only guaranteed freely available at port reservation time.
 */
public interface PortReservationService {

    /**
     * Reserve a port for use
     *
     * @return a free port at time of method call.
     */
    Integer reservePort();

    /**
     * Cancel the reservation of the specified port, indicating the service shall make it available for future reservations.
     *
     * @param port the port to unreserve
     * @throws IllegalArgumentException if the specified port has not been reserved
     */
    void cancelPort(Integer port);

}
