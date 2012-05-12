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

import org.junit.Before;
import org.junit.Test;
import org.sonatype.sisu.litmus.testsupport.TestSupport;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DefaultPortReservationService}.
 */
public class DefaultPortReservationServiceTest
    extends TestSupport
{
    private DefaultPortReservationService portReservationService;

    @Before
    public void setUp() throws Exception {
        portReservationService = new DefaultPortReservationService();
    }

    @Test
    public void basicOperation() throws Exception {
        int port = portReservationService.reservePort();
        log(port);
        assertTrue(port >0);
        portReservationService.cancelPort(port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unreserveNonReservedPort() throws Exception {
        portReservationService.cancelPort(9999);
    }

}