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

package org.sonatype.sisu.bl.jsw;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.sonatype.sisu.goodies.common.Properties2;
import org.sonatype.sisu.goodies.common.TestAccessible;

import com.google.common.base.Throwables;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * JSW configurations file (wrapper.conf) reader/writer.
 *
 * @since 1.0
 */
public class JSWConfig
{

  public static final String WRAPPER_JAVA_MAINCLASS = "wrapper.java.mainclass";

  public static final String WRAPPER_JAVA_ADDITIONAL = "wrapper.java.additional";

  public static final String WRAPPER_JAVA_CLASSPATH = "wrapper.java.classpath";

  public static final String WRAPPER_JAVA_COMMAND = "wrapper.java.command";

  /**
   * JSW configuration file.
   * Never null.
   */
  private File config;

  /**
   * Original content of JSW configuration file.
   * Null in case that JSW configuration file does not exist.
   */
  private String configContent;

  /**
   * Properties that are loaded from System, just before the configuration file is loaded.
   */
  private Properties wrapperSystemOverrides;

  /**
   * Properties read from JSW configuration file.
   * Never null.
   */
  private Properties configProperties;

  /**
   * Properties that are added/overrides.
   * Never null.
   */
  private Properties overrideProperties;

  /**
   * Combined config/override properties where override ones takes precedence.
   * Never null.
   */
  private Properties combinedProperties;

  /**
   * Comment that is written out before the override properties are written.
   * Never null.
   */
  private String overrideComment = "";

  /**
   * Flag to allow using system properties to override wrapper.* properties
   */
  private boolean wrapperSystemOverridesEnabled = true;

  /**
   * Constructor. It will use a default comment.
   *
   * @param config JSW configuration file to be read/written
   */
  public JSWConfig(final File config) {
    this(config, null);
  }

  /**
   * Constructor.
   *
   * @param config          JSW configuration file to be read/written
   * @param overrideComment comment that is written out before the override properties are written
   */
  public JSWConfig(final File config, final String overrideComment) {
    this.config = config;
    if (overrideComment != null) {
      this.overrideComment = overrideComment;
    }
    else {
      this.overrideComment = "Added by sisu-bundle-launcher";
    }
    configProperties = new Properties();
    overrideProperties = new Properties();
    combinedProperties = new Properties(configProperties);
  }

  /**
   * Reads JSW configuration file.
   *
   * @return itself, for fluent API usage
   * @throws java.io.IOException in case of reading configuration file fails
   */
  public JSWConfig load()
      throws IOException
  {
    if (config.exists()) {
      if (isWrapperSystemOverridesEnabled()) {
        wrapperSystemOverrides = wrapperPropertiesFromSystem();
      }

      InputStream in = null;
      try {
        in = new BufferedInputStream(new FileInputStream(config));
        configProperties.load(in);
        configContent = readFileToString(config);
      }
      finally {
        if (in != null) {
          in.close();
        }
      }
    }
    return this;
  }

  /**
   * Saves original + overridden JSW configuration to file provided in constructor, in case that there are any
   * changes (overridden properties).
   *
   * @return itself, for fluent API usage
   * @throws java.io.IOException in case of writing configuration file fails
   */
  public JSWConfig save()
      throws IOException
  {
    if (!overrideProperties.isEmpty() || !wrapperSystemOverrides.isEmpty()) {
      PrintWriter out = null;
      try {
        if (!config.getParentFile().mkdirs() && !config.getParentFile().exists()) {
          throw new IOException("Cannot create parent directory " + config.getAbsolutePath());
        }

        out = new PrintWriter(new FileWriter(config));

        if (configContent != null) {
          out.println(configContent);
          out.println();
        }
        out.println("# " + overrideComment);

        if (!wrapperSystemOverrides.isEmpty()) {
          out.println("#  system overrides");
          Collection<String> propNames = Properties2.sortKeys(wrapperSystemOverrides);
          for (final String propName : propNames) {
            out.println(propName + "=" + wrapperSystemOverrides.getProperty(propName));
          }
        }

        if (!overrideProperties.isEmpty()) {
          out.println("#  explicit overrides");
          Collection<String> propNames = Properties2.sortKeys(overrideProperties);
          for (final String propName : propNames) {
            out.println(propName + "=" + overrideProperties.getProperty(propName));
          }
        }

      }
      finally {
        if (out != null) {
          out.close();
        }
      }
    }
    return this;
  }

  /**
   * Returns a JSW configuration property by its key.
   *
   * @param key key of property
   * @return value of property or null if no such variable exists
   */
  public String getProperty(String key) {
    return combinedProperties.getProperty(key);
  }

  /**
   * Sets value of a JSW configuration property that will be written out as an override property.
   *
   * @param key   key of property
   * @param value value of property
   * @return itself, for fluent API usage
   */
  public JSWConfig setProperty(final String key, final String value) {
    overrideProperties.setProperty(key, value);
    combinedProperties.setProperty(key, value);
    return this;
  }

  /**
   * Adds an indexed JSW configuration property that will be written out as an override property.
   *
   * @param key   key of property
   * @param value value of property
   * @return itself, for fluent API usage
   */
  public JSWConfig addIndexedProperty(final String key, final String value) {
    int index = 0;
    for (String propertyName : combinedProperties.stringPropertyNames()) {
      try {
        int pos = Integer.valueOf(propertyName.replaceFirst(key + ".", ""));
        index = Math.max(pos, index);
      }
      catch (NumberFormatException ignore) {
        // ignore as is not an indexed property
      }
    }
    return setProperty(key + "." + ++index, value);
  }

  /**
   * Adds a Java system property.
   *
   * @param key   key of system property
   * @param value value of system property
   * @return itself, for fluent API usage
   */
  public JSWConfig addJavaSystemProperty(final String key, final String value) {
    return addJavaStartupParameter(format("-D%s=%s", key, value));
  }

  /**
   * Adds a set of Java system property.
   *
   * @param systemProperties system properties to be added. Cannot be null.
   * @return itself, for fluent API usage
   */
  public JSWConfig addJavaSystemProperties(final Map<String, String> systemProperties) {
    if (!checkNotNull(systemProperties.isEmpty())) {
      for (final Map.Entry<String, String> entry : systemProperties.entrySet()) {
        addJavaSystemProperty(
            entry.getKey(), entry.getValue() == null ? "true" : entry.getValue()
        );
      }
    }
    return this;
  }

  /**
   * Adds an additional Java startup param.
   *
   * @param parameter value of startup param
   * @return itself, for fluent API usage
   */
  public JSWConfig addJavaStartupParameter(final String parameter) {
    return addIndexedProperty(WRAPPER_JAVA_ADDITIONAL, parameter);
  }

  /**
   * Adds a set of Java startup parameters.
   *
   * @param parameters startup parameters. Cannot be null.
   * @return itself, for fluent API usage
   */
  public JSWConfig addJavaStartupParameters(final Collection<String> parameters) {
    if (!checkNotNull(parameters.isEmpty())) {
      for (final String entry : parameters) {
        addJavaStartupParameter(entry);
      }
    }
    return this;
  }

  /**
   * Sets the value of the wrapper.java.command property which should be an explicit path to a java executable.
   *
   * @param pathToJavaExecutable the value to set
   * @return itself, for fluent API
   */
  public JSWConfig setJavaCommand(final String pathToJavaExecutable) {
    return setProperty(WRAPPER_JAVA_COMMAND, pathToJavaExecutable);
  }

  public JSWConfig setJavaMainClass(final String mainClass) {
    return setProperty(WRAPPER_JAVA_MAINCLASS, mainClass);
  }

  public JSWConfig setJavaMainClass(final Class mainClass) {
    return setJavaMainClass(mainClass.getName());
  }

  public JSWConfig addToJavaClassPath(final String entry) {
    return addIndexedProperty(WRAPPER_JAVA_CLASSPATH, entry);
  }

  public JSWConfig addToJavaClassPath(final Class clazz) {
    final URL jar = clazz.getProtectionDomain().getCodeSource().getLocation();
    return addToJavaClassPath(urlToFile(jar).getAbsolutePath());
  }

  /**
   * Indicates if System properties will be checked at load time for wrapper properties.
   *
   * @return true (default) if during {@link #load()} properties starting with {@code wrapper.*} will be extracted from
   * {@link System#getProperties()}
   */
  public boolean isWrapperSystemOverridesEnabled() {
    return wrapperSystemOverridesEnabled;
  }

  /**
   * Configure if during {@link #load()} properties starting with {@code wrapper.*} will be extracted from
   * {@link System#getProperties()}
   * 
   * @param wrapperSystemOverridesEnabled whether to enable the wrapper system override
   */
  public void setWrapperSystemOverridesEnabled(final boolean wrapperSystemOverridesEnabled) {
    this.wrapperSystemOverridesEnabled = wrapperSystemOverridesEnabled;
  }

  @TestAccessible
  File urlToFile(final URL url) {
    try {
      final URI uri = url.toURI();
      return new File(uri);
    }
    catch (URISyntaxException e) {
      throw Throwables.propagate(e);
    }
  }

  /**
   * @return all system properties that start with "wrapper."
   */
  private Properties wrapperPropertiesFromSystem() {
    Properties props = new Properties();
    props.putAll(System.getProperties());
    Collection<String> propNames = Properties2.sortKeys(props);
    for (final String propName : propNames) {
      if (!propName.startsWith("wrapper.")) {
        props.remove(propName);
      }
    }
    return props;
  }

}
