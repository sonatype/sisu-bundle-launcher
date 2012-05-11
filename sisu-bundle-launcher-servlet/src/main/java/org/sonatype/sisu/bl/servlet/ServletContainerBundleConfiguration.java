/*
 * Copyright (c) 2008-2012 Sonatype, Inc. All rights reserved.
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
package org.sonatype.sisu.bl.servlet;

import java.util.List;

import org.sonatype.sisu.bl.BundleConfiguration;

/**
 * TODO
 *
 * @since 1.0
 */
public interface ServletContainerBundleConfiguration<SCBC extends ServletContainerBundleConfiguration>
    extends BundleConfiguration<SCBC>
{

    List<WAR> getWARs();

    SCBC setWARs( List<WAR> wars );

    SCBC setWARs( WAR... wars );

    SCBC addWARs( WAR... wars );

}
