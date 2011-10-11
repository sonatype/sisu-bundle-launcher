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
package org.sonatype.sisu.filetasks;

import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;
import org.sonatype.sisu.litmus.testsupport.hamcrest.FileMatchers;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests creating directories
 */
public class CreateDirectoryTest extends FileTaskTest{

    @Test
    public void createDirectoryThatDoesNotAlreadyExist()
    {
        assertThat( testMethodTargetFile( "mynewdir" ), not(FileMatchers.isDirectory()));
        run(builder().create().directory( file( testMethodTargetFile( "mynewdir" ) ) ) );
        assertThat( testMethodTargetFile( "mynewdir" ), FileMatchers.isDirectory());
    }

    @Test
    public void createDirectoryThatAlreadyExists()
    {
        run(builder().create().directory( file( testMethodTargetFile( "dir02" ) ) ) );
        assertThat( testMethodTargetFile( "dir02" ), FileMatchers.isDirectory());
        // causes no error
        run(builder().create().directory( file( testMethodTargetFile( "dir02" ) ) ) );
    }


}
