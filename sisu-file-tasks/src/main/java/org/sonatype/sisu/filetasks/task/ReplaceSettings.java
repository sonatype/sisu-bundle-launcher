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
package org.sonatype.sisu.filetasks.task;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * Replacement task settings.
 *
 * @see org.sonatype.sisu.filetasks.task.ReplaceInFilesFromDirectoryTask
 * @see org.sonatype.sisu.filetasks.task.ReplaceInFileTask
 * @since 1.3
 */
public interface ReplaceSettings
    extends FileTask
{

    /**
     * Adds a replacement.
     *
     * @param value       to be replaced
     * @param replacement to replace with
     * @return itself, for fluent API usage
     */
    ReplaceSettings addReplacement( String value, String replacement );

    /**
     * Whether or not if replace should fail when no replacement has been made.
     *
     * @param fail true/false if replace should fail when no replacement has been made
     * @return itself, for fluent API usage
     */
    ReplaceSettings setFailIfNoReplacementWasMade( boolean fail );

}
