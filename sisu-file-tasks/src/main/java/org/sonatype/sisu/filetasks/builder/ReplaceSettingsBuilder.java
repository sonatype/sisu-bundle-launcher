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

package org.sonatype.sisu.filetasks.builder;

import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.task.ReplaceSettings;

/**
 * {@link ReplaceSettings} builder.
 *
 * @since 1.3
 */
public interface ReplaceSettingsBuilder<B extends ReplaceSettingsBuilder>
    extends FileTask
{

    /**
     * Adds a replacement mapping.
     *
     * @param value       to be replaced
     * @param replacement to replace with
     * @return itself, for fluent API usage
     */
    B replace( String value, String replacement );

    /**
     * Specifies that replace should fail when no replacement has been made.
     *
     * @return itself, for fluent API usage
     */
    B failIfNoReplacementWasMade();

    /**
     * Specifies that replace should not fail when no replacement has been made.
     *
     * @return itself, for fluent API usage
     */
    B doNotFailIfNoReplacementWasMade();

}
