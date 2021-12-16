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

import org.sonatype.sisu.goodies.testsupport.TestSupport;

import com.google.common.collect.Range;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

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
    assertTrue(port > 0);
    portReservationService.cancelPort(port);
  }

  @Test(expected = IllegalArgumentException.class)
  public void unreserveNonReservedPort() throws Exception {
    portReservationService.cancelPort(9999);
  }

  @Test
  public void blockedSetOfPortsShouldNeverBeReservable() throws Exception {
    portReservationService = spy(new DefaultPortReservationService(Sets.newSet(1)));
    doReturn(1).when(portReservationService).findFreePort();
    assertCannotReservePort();

    // opposite
    portReservationService = spy(new DefaultPortReservationService());
    doReturn(1).when(portReservationService).findFreePort();
    assertReservePort(portReservationService, 1);
  }

  @Test
  public void blockedRangeOfPortsShouldNeverBeReservable() throws Exception {
    portReservationService = spy(new DefaultPortReservationService(Range.closed(1, 1)));
    doReturn(1).when(portReservationService).findFreePort();
    assertCannotReservePort();

    // opposite
    portReservationService = spy(new DefaultPortReservationService());
    doReturn(1).when(portReservationService).findFreePort();
    assertReservePort(portReservationService, 1);
  }

  @Test
  public void addedBlockedRangeForPreviouslyUnsetRangeIsBlocked() throws Exception {
    portReservationService = spy(new DefaultPortReservationService());
    doReturn(1).when(portReservationService).findFreePort();
    portReservationService.addBlockedPorts(Range.closed(1, 1));
    assertCannotReservePort();
  }

  @Test
  public void addedBlockedRangeIntersectingPreviouslyAddedRangeBlocksReservation() throws Exception {
    portReservationService = spy(new DefaultPortReservationService());
    portReservationService.addBlockedPorts(Range.closed(2, 5));
    portReservationService.addBlockedPorts(Range.closed(3, 7));

    // resulting range of blocked ports should be 3-5
    doReturn(4).when(portReservationService).findFreePort();

    assertCannotReservePort();
  }

  @Test
  public void addedBlockedRangeDisparateFromPreviouslyAddedRangeBlocksReservation() throws Exception {
    portReservationService = spy(new DefaultPortReservationService());
    portReservationService.addBlockedPorts(Range.closed(2, 5));
    portReservationService.addBlockedPorts(Range.closed(7, 10));

    doReturn(4).when(portReservationService).findFreePort();
    assertCannotReservePort();

    doReturn(8).when(portReservationService).findFreePort();
    assertCannotReservePort();

    doReturn(6).when(portReservationService).findFreePort();
    assertReservePort(portReservationService, 6);

  }

  @Test
  public void multipleRangesCanBlocked() throws Exception {
    portReservationService = spy(new DefaultPortReservationService());
    portReservationService.addBlockedPorts(Range.closed(2, 5));
    portReservationService.addBlockedPorts(Range.closed(7, 10));

    portReservationService.addBlockedPorts(Range.closed(9, 12));

    doReturn(2).when(portReservationService).findFreePort();
    assertCannotReservePort();

    doReturn(7).when(portReservationService).findFreePort();
    assertCannotReservePort();

    doReturn(11).when(portReservationService).findFreePort();
    assertCannotReservePort();

    doReturn(1).when(portReservationService).findFreePort();
    assertReservePort(portReservationService, 1);

    doReturn(13).when(portReservationService).findFreePort();
    assertReservePort(portReservationService, 13);

  }

  private void assertCannotReservePort() {
    try {
      portReservationService.reservePort();
      assertThat("Expected a port to be blocked", true, is(false));
    }
    catch (RuntimeException re) {
      //expected
    }
  }

  private void assertReservePort(DefaultPortReservationService s, int port) {
    try {
      assertThat(s.reservePort(), is(port));
    }
    catch (RuntimeException re) {
      assertThat("Expected a port to be available", true, is(false));
    }
  }


}
