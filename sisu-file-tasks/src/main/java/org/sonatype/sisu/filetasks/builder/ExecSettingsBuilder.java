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
package org.sonatype.sisu.filetasks.builder;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * Exec settings builder (included in {@link ExecSpawnBuilder}.
 *
 * @since 1.3
 */
public interface ExecSettingsBuilder<B extends ExecSettingsBuilder>
    extends FileTask
{

    B on( final FileRef directory );

    B executable( FileRef executable );

    B executable( String executable );

    B script( FileRef executable );

    B script( String executable );

    B withArgument( String argument );

    B withArguments( List<String> arguments );

    B withArguments( String[] arguments );

    B useArguments( List<String> arguments );

    B useArguments( String[] arguments );

    B withEnv( String name, String value );

    B withEnv( Map<String, String> env );

    B useEnv( Map<String, String> env );

    B useSameJavaHome();

}
