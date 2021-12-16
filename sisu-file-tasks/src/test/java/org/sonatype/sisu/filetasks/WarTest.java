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
package org.sonatype.sisu.filetasks;

import java.io.IOException;
import java.util.jar.JarFile;
import org.junit.Test;
import org.sonatype.sisu.filetasks.support.FileTaskTest;
import static org.sonatype.sisu.goodies.testsupport.hamcrest.IsNot.not;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.goodies.testsupport.hamcrest.FileMatchers.containsEntry;


import static org.hamcrest.MatcherAssert.*;

/**
 * Tests for WarBuilder
 *
 */
public class WarTest extends FileTaskTest {

    @Test
    public void updateWar() throws IOException {
        assertThat(new JarFile(testClassSourceFile("warToUpdate.war")), not(containsEntry("WEB-INF/lib/myjar.jar")));
        run(builder().war(file(testClassSourceFile("warToUpdate.war"))).addLibs(testClassSourceFile("libdir"), "**/**"));
        assertThat(new JarFile(testClassSourceFile("warToUpdate.war")), containsEntry("WEB-INF/lib/myjar.jar"));
    }

}
