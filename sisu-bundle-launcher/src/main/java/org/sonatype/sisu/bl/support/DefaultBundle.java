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

import org.apache.tools.ant.DirectoryScanner;
import org.sonatype.sisu.bl.Bundle;
import org.sonatype.sisu.bl.BundleConfiguration;
import org.sonatype.sisu.bl.internal.support.BundleLifecycle;
import org.sonatype.sisu.filetasks.FileTask;
import org.sonatype.sisu.filetasks.FileTaskBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.sisu.filetasks.FileTaskRunner.onDirectory;
import static org.sonatype.sisu.filetasks.builder.FileRef.file;
import static org.sonatype.sisu.filetasks.builder.FileRef.path;

/**
 * Default bundle implementation.
 *
 * @since 1.0
 */
@Named
public abstract class DefaultBundle<B extends Bundle, BC extends BundleConfiguration>
        extends BundleLifecycle<B, BC>
        implements Bundle<B, BC> {

    /**
     * Bundle name.
     */
    private String name;

    /**
     * File tasks builder used to manipulate files necessary to prepare bundle target directory.
     * Cannot be null.
     */
    @Inject
    private FileTaskBuilder fileTasksBuilder;

    /**
     * Configuration provider used to create default bundle configurations.
     * Cannot be null.
     */
    @Inject
    private Provider<BC> configurationProvider;

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
    private BC configuration;

    /**
     * True if application is running, false otherwise.
     */
    private boolean running;

    /**
     * Constructor. Creates the bundle with a default configuration and a not running state.
     *
     * @param name application name
     */
    @Inject
    public DefaultBundle(final String name) {
        this.name = checkNotNull(name);
    }

    /**
     * Starts application and waits for it to boot. if successfully started sets the state to running.
     * <p/>
     * {@inheritDoc}
     *
     * @throws Exception if a problem occurred during startup of application, wait period or it could not determine if
     *                   application is started in specified timeout
     * @see Bundle#start()
     */
    @Override
    public void doStart() {
        try {
            startApplication();
            running = true;
            runningBundles.add(this);
            waitForBoot();
        } catch (RuntimeException e) {
            doStop();
            throw e;
        }
    }

    /**
     * Stops application, if running.
     * <p/>
     * {@inheritDoc}
     *
     * @see Bundle#stop()
     */
    @Override
    public void doStop() {
        if (running) {
            try {
                stopApplication();
            } finally {
                unconfigure();
                running = false;
                runningBundles.remove(this);
            }
        }
    }

    /**
     * Prepares application target directory for running by:<br/>
     * - ensuring a valid configuration<br/>
     * - cleanup of target directory<br/>
     * - unpacking bundle<br/>
     * - configure<br/>
     * - applying overlays
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public void doPrepare() {
        log().debug("Using configuration {}", getConfiguration());
        validateConfiguration();
        createBundle();
        renameApplicationDirectory();
        configure();
        applyOverlays();
    }

    @Override
    public void doClean() {
        deleteTarget();
    }

    @Override
    public BC getConfiguration() {
        if (this.configuration == null) {
            this.configuration = configurationProvider.get();
            if (configuration.getId() == null) {
                configuration.setId(name + "-" + UUID.randomUUID().toString());
            }
        }
        return configuration;
    }

    @Override
    public B setConfiguration(BC configuration) {
        this.configuration = configuration;
        return (B) this;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * Starts the application.
     */
    protected abstract void startApplication();

    /**
     * Stops the application.
     */
    protected abstract void stopApplication();

    /**
     * Template method for subclasses to perform configuration tasks when bundle is starting, if necessary.
     */
    protected void configure() {
        // template method
    }

    /**
     * Template method for subclasses to perform un-configuration tasks when bundle is stopping, if necessary.
     */
    protected void unconfigure() {
        // template method
    }

    /**
     * Checks if application is alive.
     *
     * @return true if application is alive, false otherwise
     */
    protected boolean applicationAlive() {
        return true;
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
            onDirectory(config.getTargetDirectory()).apply(
                    fileTasksBuilder.rename(path(dirs[0]))
                            .to(name)
            );
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
                    logApplicationIsAlive();
                    log().debug("Application {} started in {} seconds", getName(), (System.currentTimeMillis() - start) / 1000);
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
     * Template method to eventually log the fact that application is alive.
     */
    protected void logApplicationIsAlive() {
        //template method
    }

    /**
     *
     * @return the name this bundle was created with
     */
    protected String getName() {
        return name;
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
        onDirectory(getConfiguration().getTargetDirectory()).apply(
                fileTasksBuilder.delete().directory(path("/"))
        );
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
            onDirectory(config.getTargetDirectory()).apply(
                    fileTasksBuilder.copy().directory(file(bundle))
                            .to().directory(path("/"))
            );
        } else {
            onDirectory(config.getTargetDirectory()).apply(
                    fileTasksBuilder.expand(file(bundle))
                            .to().directory(path("/"))
            );
        }
    }

    /**
     * Applies overlays to target directory.
     */
    private void applyOverlays() {
        BundleConfiguration config = getConfiguration();
        List<FileTask> overlays = config.getOverlays();
        if (overlays == null) {
            return;
        }
        onDirectory(config.getTargetDirectory()).apply(overlays);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" bundle");
        sb.append(" [id: ").append(getConfiguration().getId()).append("]");
        sb.append(isRunning() ? " [running]" : " [not running]");
        return sb.toString();
    }

}
