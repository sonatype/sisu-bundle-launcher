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

package org.sonatype.sisu.bl.support.resolver;

import com.google.inject.Binder;
import org.junit.Test;
import org.sonatype.sisu.litmus.testsupport.inject.InjectedTestSupport;
import org.sonatype.sisu.maven.bridge.MavenArtifactResolver;

import javax.inject.Inject;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

/**
 * TODO
 *
 * @since 1.0
 */
public class MavenBridgedBundleResolverTest
        extends InjectedTestSupport {

    @Inject
    private MavenBridgedBundleResolver resolver;

    @Override
    public void configure(final Properties properties) {
        super.configure(properties);
        properties.put(MavenBridgedBundleResolver.BUNDLE_COORDINATES, "group:artifact:version");
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(MavenArtifactResolver.class).toInstance(mock(MavenArtifactResolver.class));
    }

    @Test
    public void canInject() {
        assertThat(resolver, is(notNullValue()));
    }


}