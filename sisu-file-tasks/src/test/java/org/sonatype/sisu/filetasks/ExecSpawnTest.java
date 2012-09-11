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
package org.sonatype.sisu.filetasks;

import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

import java.io.File;

import org.junit.Test;
import org.sonatype.sisu.filetasks.builder.ExecSpawnBuilder;
import org.sonatype.sisu.filetasks.support.FileTaskTest;
import org.sonatype.sisu.filetasks.task.ExecSpawnTask;

/**
 * {@link ExecSpawnTask} and {@link ExecSpawnBuilder} unit tests.
 *
 * @since 1.3
 */
public class ExecSpawnTest
    extends FileTaskTest
{

    @Test
    public void exec01()
    {
        builder().exec().spawn()
            .on( file( new File( "bar" ) ) )
            .executable( "foo" )
            .withArgument( "1" )
            .withArgument( "2" )
            .withEnv( "FOO", "BAR" );
    }

    @Test
    public void exec02()
    {
        builder().exec().spawn()
            .on( file( new File( "bar" ) ) )
            .script( path( "bin/foo" ) )
            .useSameJavaHome();
    }

}
