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
package org.sonatype.sisu.bl.support;

import org.apache.tools.ant.DirectoryScanner;
import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.BundleConfiguration;
import org.sonatype.sisu.bl.internal.DefaultBundleState;
import org.sonatype.sisu.bl.internal.support.BundleLifecycle;
import org.sonatype.sisu.bl.support.jsw.JSWExecFactory;
import org.sonatype.sisu.bl.support.port.PortReservationService;
import org.sonatype.sisu.overlay.Overlay;
import org.sonatype.sisu.overlay.OverlayBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default bundle implementation.
 *
 * @since 1.0
 */
@Named
public abstract class DefaultBundle<B extends Bundle, C extends BundleConfiguration>
        extends BundleLifecycle<B, C>
        implements Bundle<B, C> {

    /**
     * Bundle name.
     */
    private String name;

    /**
     * Overlay builder used to create overlays necessary to prepare bundle target directory.
     * Cannot be null.
     */
    @Inject
    private OverlayBuilder overlayBuilder;

    /**
     * JSW Executor factory.
     * Cannot be null.
     */
    @Inject
    private JSWExecFactory jswExecFactory;

    /**
     * Port reservation service used to generate an random port to be used by running application.
     * Cannot be null.
     */
    @Inject
    private PortReservationService portReservationService;

    /**
     * Configuration provider used to create default bundle configurations.
     * Cannot be null.
     */
    @Inject
    private Provider<C> configurationProvider;

    /**
     * List of running bundles.
     * Cannot be null.
     */
    @Inject
    private RunningBundles runningBundles;

    /**
     * Bundle configuration.
     * Cannot be null.
     */
    private C configuration;

    /**
     * Running state.
     * Cannot be null.
     */
    private DefaultBundleState state;

    /**
     * Constructor. Creates the bundle with a default configuration and a not running state.
     *
     * @param name application name
     * @since 1.0
     */
    @Inject
    public DefaultBundle(final String name) {
        this.name = checkNotNull(name);
        state = new DefaultBundleState();
    }

    /**
     * Starts application and waits for it to boot. if successfully started sets the state to running.
     * <p/>
     * {@inheritDoc}
     *
     * @throws Exception if a problem occurred during startup of application, wait period or it could not determine if
     *                   application is started in specified timeout
     * @see Bundle#start()
     * @since 1.0
     */
    @Override
    public void doStart() {
        try {
            startApplication();
            getState().setRunning(true);
            runningBundles.add( this );
            waitForBoot();
        } catch (RuntimeException e) {
            doStop();
            throw (RuntimeException) e;
        }
    }

    /**
     * Stops application if running, returns the used port to port reservation service and sets the state to not running.
     * <p/>
     * {@inheritDoc}
     *
     * @see Bundle#stop()
     * @since 1.0
     */
    @Override
    public void doStop() {
        try {
            if (getState().isRunning()) {
                stopApplication();
            }
        } finally {
            unconfigurePort();
            getState().reset();
            runningBundles.remove( this );
        }
    }

    /**
     * Prepares application target directory for running by:<br/>
     * - ensuring a valid configuration<br/>
     * - cleanup of target directory<br/>
     * - unpacking bundle<br/>
     * - configure a random port using port reservation service<br/>
     * - applying overlays
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public void doPrepare() {
        log().info("Using configuration {}", getConfiguration());
        validateConfiguration();
        createBundle();
        renameApplicationDirectory();
        configurePort();
        applyOverlays();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public void doClean() {
        deleteTarget();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public C getConfiguration() {
        if (this.configuration == null) {
            this.configuration = configurationProvider.get();
            if (configuration.getId() == null) {
                configuration.setId(name + "-" + UUID.randomUUID().toString());
            }
        }
        return configuration;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public B setConfiguration(C configuration) {
        this.configuration = configuration;
        return (B) this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public DefaultBundleState getState() {
        return state;
    }

    /**
     * Starts the application.
     *
     * @since 1.0
     */
    protected abstract void startApplication();

    /**
     * Stops the application.
     *
     * @since 1.0
     */
    protected abstract void stopApplication();

    /**
     * Composes application URL in format {@code http://localhost:<port>/<aplication name>}.
     *
     * @return application URL
     * @since 1.0
     */
    protected String composeApplicationURL() {
        return String.format("http://localhost:%s/%s", getState().getPort(), name);
    }

    /**
     * Checks if application is alive by accessing state provided URL and checking that it responds with 200 OK.
     *
     * @return true if application is alive, false otherwise
     * @since 1.0
     */
    protected boolean applicationAlive() {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) getState().getUrl().openConnection();
            urlConnection.setConnectTimeout(0);
            urlConnection.setReadTimeout(0);
            urlConnection.setUseCaches(false);
            urlConnection.connect();
            return urlConnection.getResponseCode() == 200;
        } catch (IOException ignore) {
            return false;
        }
    }

    /**
     * Renames application directory that usually has a name that contains the version to a name easy to be used in
     * overlays = application name.
     */
    private void renameApplicationDirectory() {
        BundleConfiguration config = getConfiguration();

        DirectoryScanner ds = new DirectoryScanner();
        ds.setBasedir(config.getTargetDirectory());
        ds.setIncludes(new String[]{name + "-*"});
        ds.scan();
        String[] dirs = ds.getIncludedDirectories();

        if (dirs.length == 1 && new File(config.getTargetDirectory(), dirs[0]).exists()) {
            overlayBuilder.rename()
                    .from(dirs[0])
                    .to(name)
                    .applyTo(config.getTargetDirectory());
        }
    }

    /**
     * Waits for application to boot for configured timeout period.
     */
    private void waitForBoot() {
        long start = System.currentTimeMillis();
        int startTimeout = getConfiguration().getStartTimeout();

        log().info("Waiting for application to boot for {} seconds", startTimeout);

        while (System.currentTimeMillis() < start + startTimeout * 1000) {
            try {
                if (applicationAlive()) {
                    log().info("Application is running at {} (started in {} seconds)", getState().getUrl().toExternalForm(), (System.currentTimeMillis() - start) / 1000);
                    return;
                }
                Thread.sleep(Math.min(startTimeout, 1000));
            } catch (InterruptedException ignore) {
                // ignore
            }
        }
        throw new RuntimeException("Could not detect application running in the configured timeout of " + startTimeout + " seconds");
    }

    /**
     * Validates configuration:<br/>
     * - id is set
     * - bundle is set<br/>
     * - target directory is set
     *
     * @throws RuntimeException if any of above is not true
     */
    private void validateConfiguration() throws RuntimeException {
        BundleConfiguration config = getConfiguration();
        if (config.getId() == null || config.getId().trim().length() == 0) {
            throw new RuntimeException("Id must be set in bundle configuration");
        }
        if (config.getBundle() == null) {
            log().warn("There is no bundle to be created.");
        }
        if (config.getTargetDirectory() == null) {
            throw new RuntimeException("Target directory must be set in bundle configuration");
        }
    }

    /**
     * Deletes target directory.
     */
    private void deleteTarget() {
        overlayBuilder.delete()
                .directory("/")
                .applyTo(getConfiguration().getTargetDirectory());
    }

    /**
     * Creates application in target directory by unpacking the bundle or coping it if bundle is a directory.
     */
    private void createBundle() {
        BundleConfiguration config = getConfiguration();
        File bundle = config.getBundle();
        if (bundle == null) {
            return;
        }
        if (bundle.isDirectory()) {
            overlayBuilder.overlayDirectory(bundle)
                    .over().directory("/")
                    .applyTo(config.getTargetDirectory());
        } else {
            overlayBuilder.expand(bundle)
                    .to().directory("/")
                    .applyTo(config.getTargetDirectory());
        }
    }

    /**
     * Applies overlays to target directory.
     */
    private void applyOverlays() {
        BundleConfiguration config = getConfiguration();
        List<Overlay> overlays = config.getOverlays();
        if (overlays == null) {
            return;
        }
        for (Overlay overlay : overlays) {
            overlay.applyTo(config.getTargetDirectory());
        }
    }

    /**
     * Reserves a port form port reservation service and add creates a JSW configuration file specifying jetty port as
     * a system property.
     *
     * @throws RuntimeException if a problem occurred during reading of JSW configuration or writing the additional JSW
     *                          configuration file
     */
    private void configurePort() throws RuntimeException {
        try {
            getState().setPort(portReservationService.reservePort());
            getState().setUrl(new URL(composeApplicationURL()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cancels reserved port.
     */
    private void unconfigurePort() {
        if (getState().getPort() > 0) {
            portReservationService.cancelPort(getState().getPort());
        }
    }

}
