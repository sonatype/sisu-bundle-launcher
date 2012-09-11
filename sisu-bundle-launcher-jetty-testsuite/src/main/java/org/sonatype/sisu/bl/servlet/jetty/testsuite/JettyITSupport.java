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
package org.sonatype.sisu.bl.servlet.jetty.testsuite;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.sisu.bl.servlet.jetty.JettyBundle;
import org.sonatype.sisu.bl.support.resolver.TargetDirectoryResolver;
import org.sonatype.sisu.litmus.testsupport.TestData;
import org.sonatype.sisu.litmus.testsupport.TestIndex;
import org.sonatype.sisu.litmus.testsupport.inject.InjectedTestSupport;
import org.sonatype.sisu.litmus.testsupport.junit.TestDataRule;
import org.sonatype.sisu.litmus.testsupport.junit.TestIndexRule;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.google.inject.Binder;

/**
 * Support class for Jetty based ITs.
 *
 * @since 1.4
 */
public class JettyITSupport
    extends InjectedTestSupport
{

    static final Logger LOGGER = LoggerFactory.getLogger( JettyITSupport.class );

    /**
     * Test index.
     * Never null.
     */
    @Rule
    public TestIndexRule testIndex = new TestIndexRule( util.resolveFile( "target/its" ) );

    /**
     * Test data.
     * Never null.
     */
    @Rule
    public TestDataRule testData = new TestDataRule( util.resolveFile( "src/test/it-resources" ) );

    @Override
    public void configure( final Binder binder )
    {
        binder.bind( TargetDirectoryResolver.class ).toInstance(
            new TargetDirectoryResolver()
            {
                @Override
                public File resolve()
                {
                    return testIndex.getDirectory();
                }
            }
        );
    }

    /**
     * Returns test data accessor.
     *
     * @return test data accessor. Never null.
     */
    public TestData testData()
    {
        return testData;
    }

    /**
     * Returns test index.
     *
     * @return test index. Never null.
     */
    public TestIndex testIndex()
    {
        return testIndex;
    }

    static Stopwatch startJetty( final JettyBundle jettyBundle )
    {
        final Stopwatch stopwatch = new Stopwatch();
        if ( jettyBundle != null && !jettyBundle.isRunning() )
        {
            stopwatch.start();
            try
            {
                LOGGER.info( "Starting Jetty ({})", jettyBundle );
                jettyBundle.start();
            }
            catch ( Exception e )
            {
                throw Throwables.propagate( e );
            }
            stopwatch.stop();
        }
        return stopwatch;
    }

    static void stopJetty( final JettyBundle jettyBundle )
    {
        if ( jettyBundle != null && jettyBundle.isRunning() )
        {
            try
            {
                LOGGER.info( "Stopping Jetty ({})", jettyBundle );
                jettyBundle.stop();
            }
            catch ( Exception e )
            {
                throw Throwables.propagate( e );
            }
        }
    }

    protected void recordLogs( final JettyBundle jetty )
    {
        final File logs = new File( jetty.getConfiguration().getTargetDirectory(), "jetty/logs" );
        final File[] logFiles = logs.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept( final File parentDir, final String name )
            {
                return name.contains( "log" );
            }
        });
        if ( logFiles != null && logFiles.length > 0 )
        {
            for ( File logFile : logFiles )
            {
                testIndex().recordLink( logFile.getName(), logFile );
            }
        }
    }

}