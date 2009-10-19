/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.grrrowl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.grrrowl.impl.AppleScriptGrowl;
import org.sonatype.grrrowl.impl.NativeGrowl;
import org.sonatype.grrrowl.impl.NullGrowl;

import java.lang.reflect.Constructor;

/**
 * Provides an interface to <a href="http://growl.info">Growl</a>.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class GrowlFactory
{
    private static final Logger log = LoggerFactory.getLogger(GrowlFactory.class);

    public static final String TYPE = GrowlFactory.class.getName() + ".type";
    
    public static Growl create(String appName) {
        Growl growl = doCreate(appName);
        log.trace("Using growl: {}", growl);
        return growl;
    }

    private static Growl doCreate(String appName) {
        assert appName != null;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        String className = System.getProperty(TYPE);
        if (className != null) {
            log.debug("Loading growl class: {}", className);

            try {
                Class type = cl.loadClass(className);
                Constructor factory = type.getConstructor(String.class);
                return (Growl) factory.newInstance(appName);
            }
            catch (Exception e) {
                log.warn("Failed to construct growl of type: " + className, e);
            }
        }

        if (isMacOs()) {
            try {
                return new NativeGrowl(appName);
            }
            catch (Throwable t) {
                log.trace("Could not load native impl using default", t);
            }

            try {
                return new AppleScriptGrowl(appName);
            }
            catch (Throwable t) {
                log.trace("Could not load AppleScript impl using default", t);
            }
        }

        return new NullGrowl();
    }

    static boolean isMacOs() {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return true;
        }
        return false;
    }
}