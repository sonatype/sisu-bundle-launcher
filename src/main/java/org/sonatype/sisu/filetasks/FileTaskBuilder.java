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

import org.sonatype.sisu.filetasks.builder.ChmodBuilder;
import org.sonatype.sisu.filetasks.builder.CopyBuilder;
import org.sonatype.sisu.filetasks.builder.DeleteBuilder;
import org.sonatype.sisu.filetasks.builder.ExpandBuilder;
import org.sonatype.sisu.filetasks.builder.FileRef;
import org.sonatype.sisu.filetasks.builder.MoveBuilder;
import org.sonatype.sisu.filetasks.builder.PropertiesBuilder;
import org.sonatype.sisu.filetasks.builder.RenameBuilder;

/**
 * TODO
 *
 * @since 1.0
 */
public interface FileTaskBuilder
{

    CopyBuilder copy();

    DeleteBuilder delete();

    RenameBuilder rename( FileRef target );

    MoveBuilder move( FileRef from );

    ExpandBuilder expand( FileRef archive );

    PropertiesBuilder properties( FileRef target );

    ChmodBuilder chmod( FileRef directory );

}
