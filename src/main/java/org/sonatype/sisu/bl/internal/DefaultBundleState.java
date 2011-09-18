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
package org.sonatype.sisu.bl.internal;

import org.sonatype.sisu.bl.Bundle;

import java.net.URL;

/**
 * Java Bean implementation of {@link Bundle.State}.
 *
 * @since 1.0
 */
public class DefaultBundleState
        implements Bundle.State {

    /**
     * True if application is running, false otherwise.
     */
    private boolean running;

    /**
     * Port on which application is running. Should be 0 (zero) if application is not running.
     */
    private int port;

    /**
     * URL application is available at. Should be null if application is not running.
     */
    private URL url;

    /**
     * Creates a new instance and resets to a non running state.
     */
    public DefaultBundleState() {
        reset();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets whether application is running or not.
     *
     * @param running true if application is running, false otherwise
     * @since 1.0
     */
    public void setRunning(final boolean running) {
        this.running = running;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * Sets the port application is running on.
     *
     * @param port port application is running on
     * @since 1.0
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public URL getUrl() {
        return url;
    }

    /**
     * Sets URL application is running at.
     *
     * @param url URL application is running at
     * @since 1.0
     */
    public void setUrl(final URL url) {
        this.url = url;
    }

    /**
     * Resets to a non running state.
     *
     * @since 1.0
     */
    public void reset() {
        running = false;
        port = 0;
        url = null;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DefaultBundleState");
        sb.append("{running=").append(running);
        sb.append(", port=").append(port);
        sb.append(", url=").append(url);
        sb.append('}');
        return sb.toString();
    }

}
