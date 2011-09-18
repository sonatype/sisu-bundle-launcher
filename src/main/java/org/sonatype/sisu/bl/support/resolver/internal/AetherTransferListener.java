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

package org.sonatype.sisu.bl.support.resolver.internal;

import org.slf4j.Logger;
import org.sonatype.aether.transfer.AbstractTransferListener;
import org.sonatype.aether.transfer.TransferCancelledException;
import org.sonatype.aether.transfer.TransferEvent;

class AetherTransferListener
        extends AbstractTransferListener {

    private final Logger log;

    private ThreadLocal<Long> last;

    public AetherTransferListener(Logger log) {
        this.log = log;
        last = new ThreadLocal<Long>();
    }

    @Override
    public void transferInitiated(TransferEvent event)
            throws TransferCancelledException {
        log.info("Downloading {}{}...", event.getResource().getRepositoryUrl(), event.getResource().getResourceName());
    }

    @Override
    public void transferSucceeded(TransferEvent event) {
        log.info("Downloaded [{} bytes] {}{}", new Object[]{event.getTransferredBytes(),
                event.getResource().getRepositoryUrl(), event.getResource().getResourceName()});
    }

    @Override
    public void transferFailed(TransferEvent event) {
        log.error("Failed to download {}{}", new Object[]{event.getResource().getRepositoryUrl(),
                event.getResource().getResourceName(), event.getException()});
    }

    @Override
    public void transferProgressed(TransferEvent event)
            throws TransferCancelledException {
        Long last = this.last.get();
        if (last == null || last.longValue() < System.currentTimeMillis() - 5 * 1000) {
            String progress;
            if (event.getResource().getContentLength() > 0) {
                progress = (int) (event.getTransferredBytes() * 100.0 / event.getResource().getContentLength()) + "%";
            } else {
                progress = event.getTransferredBytes() + " bytes";
            }
            log.info("Downloading [{}] {}{}...", new Object[]{progress, event.getResource().getRepositoryUrl(),
                    event.getResource().getResourceName()});
            this.last.set(new Long(System.currentTimeMillis()));
        }
    }

}
