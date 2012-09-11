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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate test classes for customizing when Jetty is started and stopped. Possible options are each
 * method (default) and once per test class.
 *
 * @since 2.1
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
@Documented
public @interface JettyStartAndStopStrategy
{

    // fully qualify due to http://bugs.sun.com/view_bug.do?bug_id=6512707
    JettyStartAndStopStrategy.Strategy value() default JettyStartAndStopStrategy.Strategy.EACH_METHOD;

    public static enum Strategy
    {
        /**
         * Strategy to be used when you want Jetty to be started before first test method is invoked and stopped after
         * last test method was executed.
         */
        EACH_TEST,
        /**
         * Strategy to be used when you want Jetty to be started before each test method and stopped after the method
         * was executed.
         */
        EACH_METHOD;
    }

}
