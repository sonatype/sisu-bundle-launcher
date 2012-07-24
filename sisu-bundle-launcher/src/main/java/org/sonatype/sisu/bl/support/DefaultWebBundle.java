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
package org.sonatype.sisu.bl.support;

import static com.google.common.base.Preconditions.checkState;
import static org.sonatype.sisu.bl.WebBundleConfiguration.RANDOM_PORT;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.sonatype.sisu.bl.WebBundle;
import org.sonatype.sisu.bl.WebBundleConfiguration;
import org.sonatype.sisu.bl.support.port.PortReservationService;
import org.sonatype.sisu.filetasks.FileTaskBuilder;

/**
 * Default web bundle implementation.
 *
 * @since 1.0
 */
@Named
public abstract class DefaultWebBundle<WB extends WebBundle, WBC extends WebBundleConfiguration>
    extends DefaultBundle<WB, WBC>
    implements WebBundle<WB, WBC>
{

    /**
     * Port reservation service used to generate an random port to be used by running application.
     * Cannot be null.
     */
    private PortReservationService portReservationService;

    /**
     * Port on which application is running. Should be 0 (zero) if application is not running.
     */
    private int port;

    /**
     * URL application is available at. Should be null if application is not running.
     */
    private URL url;

    @Inject
    public DefaultWebBundle( final String name,
                             final Provider<WBC> configurationProvider,
                             final RunningBundles runningBundles,
                             final FileTaskBuilder fileTaskBuilder,
                             final PortReservationService portReservationService )
    {
        super( name, configurationProvider, runningBundles, fileTaskBuilder );
    }

    @Override
    public int getPort()
    {
        return port;
    }

    @Override
    public URL getUrl()
    {
        return url;
    }

    /**
     * Checks if application is alive by accessing state provided URL and checking that it responds with 200 OK.
     *
     * @return true if application is alive, false otherwise
     */
    protected boolean applicationAlive()
    {
        try
        {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout( 0 );
            urlConnection.setReadTimeout( 0 );
            urlConnection.setUseCaches( false );
            urlConnection.connect();
            return urlConnection.getResponseCode() == 200;
        }
        catch ( IOException ignore )
        {
            return false;
        }
    }

    /**
     * Logs URL where application is running.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected void logApplicationIsAlive()
    {
        log.info( "Application {} is running at {}", getName(), getUrl() );
    }

    /**
     * Reserves a port form port reservation service and add creates a JSW configuration file specifying jetty port as
     * a system property.
     *
     * @throws RuntimeException if a problem occurred during reading of JSW configuration or writing the additional JSW
     *                          configuration file
     */
    @Override
    protected void configure()
        throws Exception
    {
        if ( getConfiguration().getPort() == RANDOM_PORT )
        {
            port = portReservationService.reservePort();
        }
        else
        {
            port = getConfiguration().getPort();
        }
        url = new URL( composeApplicationURL() );
    }

    /**
     * Cancels reserved port.
     */
    @Override
    protected void unconfigure()
    {
        if ( getConfiguration().getPort() == RANDOM_PORT && port > 0 )
        {
            getPortReservationService().cancelPort( port );
        }
        port = 0;
        url = null;
    }

    /**
     * Composes application URL in format {@code http://localhost:<port>/<name>}.
     *
     * @return application URL
     */
    protected String composeApplicationURL()
    {
        return String.format( "http://localhost:%s/%s/", getPort(), getName() );
    }

    protected PortReservationService getPortReservationService()
    {
        checkState( portReservationService != null );
        return portReservationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( super.toString() );
        if ( isRunning() )
        {
            sb.append( " [" ).append( getUrl() ).append( "]" );
        }
        return sb.toString();
    }

}
