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

package org.sonatype.sisu.bl.support.jsw;

/**
 * Helper to perform operations on JSW bundle scripts.
 *
 * @since 1.0
 */
public interface JSWExec {

    /**
     * Starts the server using cmd line scripts.
     *
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    public JSWExec start();

    /**
     * Stop the server using cmd line script.
     * <p/>
     * This method is more reliable when you need the server completely stopped
     * before continuing.
     *
     * @return itself, for usage in fluent api
     * @since 1.0
     */
    public JSWExec stop();


}
