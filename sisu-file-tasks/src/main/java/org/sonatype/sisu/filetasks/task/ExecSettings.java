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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.sonatype.sisu.filetasks.FileTask;

/**
 * Exec task settings.
 *
 * @since 1.3
 */
public interface ExecSettings
    extends FileTask
{

    /**
     * Specify the working directory for the executed process.
     *
     * @param directory directory for the executed process
     * @return itself, for fluent API usage
     */
    ExecSettings setWorkingDirectory( File directory );

    /**
     * Specify the executable/script to be executed.
     *
     * @param executable executable to be executed
     * @return itself, for fluent API usage
     */
    ExecSettings setExecutable( String executable );

    /**
     * Adds an argument.
     *
     * @param argument argument
     * @return itself, for fluent API usage
     */
    ExecSettings addArgument( String argument );

    ExecSettings addArguments( List<String> arguments );

    ExecSettings addArguments( String[] arguments );

    ExecSettings setArguments( List<String> arguments );

    ExecSettings setArguments( String[] arguments );

    ExecSettings addEnv( String name, String value );

    ExecSettings addEnv( Map<String, String> env );

    ExecSettings setEnv( Map<String, String> env );

    ExecSettings setUseSameJavaHome( boolean useSameJavaHome );

}
