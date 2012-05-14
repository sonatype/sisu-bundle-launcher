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

package org.sonatype.sisu.jsw.exec;

import java.io.File;

/**
 * {@link JSWExec} factory.
 *
 * @since 1.0
 */
public interface JSWExecFactory
{

    /**
     * Creates a {@link JSWExec}.
     *
     * @param binDir  the bin directory where the jsw control scripts are located
     * @param appName the app name managed by JSW
     * @param monitorPort monitor port
     * @return created {@link JSWExec}
     * @since 1.2
     */
    JSWExec create( File binDir, String appName, int monitorPort );

}
