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
     * Port on which application is running. Should be 0 (zero) if application is not running.
     */
    private int port;

    /**
     * URL application is available at. Should be null if application is not running.
     */
    private URL url;

    /**
     * Creates the bundle with a default configuration and a not running state.
     *
     * @param name                   application name
     * @param configurationProvider  configuration provider
     * @param runningBundles         running bundles
     * @param fileTaskBuilder        file task builder
     * @param portReservationService service to reserve ports to be used by this bundle
     */
    @Inject
    public DefaultWebBundle( final String name,
                             final Provider<WBC> configurationProvider,
                             final RunningBundles runningBundles,
                             final FileTaskBuilder fileTaskBuilder,
                             final PortReservationService portReservationService )
    {
        super( name, configurationProvider, runningBundles, fileTaskBuilder, portReservationService );
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
    @Override
    protected boolean applicationAlive()
    {
        try
        {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout( 0 );
            urlConnection.setReadTimeout( 0 );
            urlConnection.setUseCaches( false );
            urlConnection.connect();
            // if connect does not result in an exception, the application is alive
            return true;
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
        log.info( "{} ({}) is running at {}", getName(), getConfiguration().getId(), getUrl() );
    }

    /**
     * Reserves a port from the {@link PortReservationService} and composes the application url.
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
     * Composes application URL in format {@code http://<hostName>:<port>/<name>}.
     *
     * @return application URL
     */
    protected String composeApplicationURL()
    {
        return String.format( "http://%s:%s/%s/", getConfiguration().getHostName(), getPort(), getName() );
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
