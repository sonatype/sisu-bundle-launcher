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
package org.sonatype.sisu.bl.support.jsw.internal;

import org.sonatype.sisu.bl.support.jsw.JSWExec;
import org.sonatype.sisu.bl.support.jsw.JSWExecFactory;
import org.sonatype.sisu.filetasks.FileTaskBuilder;
import org.sonatype.sisu.filetasks.support.AntHelper;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link JSWExecFactory} implementation.
 *
 * @since 1.0
 */
@Named
class JSWExecFactoryImpl
        implements JSWExecFactory {

    /**
     * ANT helper used by {@link JSWExecImpl}.
     */
    private AntHelper antHelper;
    /**
     * File Task builder used by {@link JSWExecImpl}.
     */
    private FileTaskBuilder fileTaskBuilder;

    /**
     * Constructor.
     *
     * @param antHelper       ANT helper used by {@link JSWExecImpl}
     * @param fileTaskBuilder file task builder used by {@link JSWExecImpl}.
     * @since 1.0
     */
    @Inject
    public JSWExecFactoryImpl(final AntHelper antHelper,
                              final FileTaskBuilder fileTaskBuilder) {
        this.fileTaskBuilder = fileTaskBuilder;
        this.antHelper = checkNotNull(antHelper);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public JSWExec create(final File binDir, final String appName) {
        return new JSWExecImpl(binDir, appName, antHelper, fileTaskBuilder);
    }

}
