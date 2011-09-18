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

import org.sonatype.inject.Nullable;
import org.sonatype.sisu.bl.BundleConfiguration;
import org.sonatype.sisu.bl.support.resolver.BundleResolver;
import org.sonatype.sisu.bl.support.resolver.TargetDirectoryResolver;
import org.sonatype.sisu.overlay.Overlay;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Default {@link BundleConfiguration} implementation that eventually (if bounded) makes use of
 * {@link BundleResolver} to resolve the bundle file and {@link TargetDirectoryResolver} to resolve target directory.
 *
 * @since 1.0
 */
@Named
public class DefaultBundleConfiguration<T extends BundleConfiguration>
        implements BundleConfiguration<T> {

    /**
     * Start timeout configuration property key.
     */
    public static final String START_TIMEOUT = "DefaultBundleConfiguration.startTimeout";

    /**
     * Default start timeout.
     */
    public static final int START_TIMEOUT_DEFAULT = 60;

    /**
     * Bundle identity.
     */
    private String id;

    /**
     * Application bundle zip/jar/tar or directory.
     */
    private File bundle;

    /**
     * Application target directory, where bundle will be exploded.
     */
    private File targetDirectory;

    /**
     * List of overlays to be applied to exploded bundle. Should never be null.
     */
    private List<Overlay> overlays;

    /**
     * Number of seconds to wait for application to boot.
     */
    private Integer startTimeout;

    /**
     * Resolver to be used to resolve application bundle file. Can be null.
     * Lazy used (if not null) when bundle file is requested.
     */
    private BundleResolver bundleResolver;

    /**
     * Resolver to be used to resolve application target directory. Can be null.
     * Lazy used (if not null) when target directory is requested.
     */
    private TargetDirectoryResolver targetDirectoryResolver;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    @Inject
    public DefaultBundleConfiguration() {
        setId(null);
        setOverlays();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public T setId(final String id) {
        this.id = id;
        return (T) this;
    }

    /**
     * Resolves the bundle (if not already set) by using {@link org.sonatype.sisu.bl.support.resolver.BundleResolver} (if present).
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public File getBundle() {
        if (bundle == null && bundleResolver != null) {
            bundle = bundleResolver.resolve();
        }
        return bundle;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public T setBundle(File bundle) {
        this.bundle = bundle;
        return (T) this;
    }

    /**
     * Resolves the target directory (if not already set) by using {@link org.sonatype.sisu.bl.support.resolver.TargetDirectoryResolver} (if present).
     * <p/>
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public File getTargetDirectory() {
        if (targetDirectory == null && targetDirectoryResolver != null) {
            targetDirectory = new File(targetDirectoryResolver.resolve(), getId());
        }
        return targetDirectory;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public T setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
        return (T) this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public List<Overlay> getOverlays() {
        return overlays;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public T setOverlays(List<Overlay> overlays) {
        this.overlays = new ArrayList<Overlay>();
        if (overlays != null) {
            this.overlays.addAll(overlays);
        }
        return (T) this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public T setOverlays(Overlay... overlays) {
        return setOverlays(Arrays.asList(overlays));
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public T addOverlays(Overlay... overlays) {
        this.overlays.addAll(Arrays.asList(overlays));
        return (T) this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public Integer getStartTimeout() {
        return startTimeout;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public T setStartTimeout(final Integer startTimeout) {
        this.startTimeout = startTimeout;
        return (T) this;
    }

    /**
     * Sets number of seconds to wait for application to boot. If injected will use the timeout bounded to
     * {@link #START_TIMEOUT} with a default of {@link #START_TIMEOUT_DEFAULT} seconds.
     *
     * @since 1.0
     */
    @Inject
    protected void configureStartTimeout(final @Named("${" + START_TIMEOUT + ":-" + START_TIMEOUT_DEFAULT + "}") Integer startTimeout) {
        setStartTimeout(startTimeout);
    }

    /**
     * Sets an optional bundle resolver.
     *
     * @param bundleResolver optional bundle resolver to be used to resolve application bundle if bundle not set
     * @since 1.0
     */
    @Inject
    protected void setBundleResolver(final @Nullable BundleResolver bundleResolver) {
        this.bundleResolver = bundleResolver;
    }

    /**
     * Sets an optional target directory resolver.
     *
     * @param targetDirectoryResolver optional target directory resolver to be used to resolve directory where
     *                                application bundle is exploded if target directory is not set
     * @since 1.0
     */
    @Inject
    protected void setTargetDirectoryResolver(final @Nullable TargetDirectoryResolver targetDirectoryResolver) {
        this.targetDirectoryResolver = targetDirectoryResolver;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("{id=").append(getId());
        sb.append(", bundle=").append(getBundle());
        sb.append(", targetDirectory=").append(getTargetDirectory());
        sb.append('}');
        return sb.toString();
    }

}
