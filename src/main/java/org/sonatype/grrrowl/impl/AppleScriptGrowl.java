/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.grrrowl.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.grrrowl.Growl;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Provides {@link Growl} support via <a href="http://growl.info/documentation/applescript-support.php">AppleScript</a>.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 *
 * @see <a href="http://growl.info/documentation/applescript-support.php">Growl AppleScript Support</a>
 */
public class AppleScriptGrowl
    implements Growl
{
    private static final Logger log = LoggerFactory.getLogger(AppleScriptGrowl.class);

    private static final String GROWL_HELPER_APP = "GrowlHelperApp";

    private static final String ENGINE_NAME = "AppleScript";

    private final ScriptEngine engine;

    private final String appName;

    private String[] notifications;

    private String[] enabled;

    public AppleScriptGrowl(final String appName) {
        assert appName != null;
        this.appName = appName;

        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName(ENGINE_NAME);
        if (engine == null) {
            throw new UnsupportedOperationException("Engine is not available: " + ENGINE_NAME);
        }
    }

    public void setAllowedNotifications(final String... notifications) {
        assert notifications != null;
        this.notifications = notifications;
    }

    public void setEnabledNotifications(final String... notifications) {
        assert notifications != null;
        this.enabled = notifications;
    }

    public boolean isGrowlRunning() {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);

        out.println("tell application \"System Events\"");
	    out.format("set isRunning to count of (every process whose name is \"%s\") > 0", GROWL_HELPER_APP).println();
        out.println("end tell");

        Object result = eval(buff);
        if (result == null) {
            return false;
        }
        else if (result instanceof Number) {
            return ((Number)result).intValue() > 0;
        }

        log.warn("Unable to decode result: {}", result);

        return false;
    }

    public void register() {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);

        out.format("tell application \"%s\"", GROWL_HELPER_APP).println();

        out.print("set the allNotificationsList to {");
        for (int i=0; i<notifications.length; i++) {
            out.format("\"%s\"", notifications[i]);
            if (i+1<notifications.length) {
                out.print(",");
            }
        }
        out.print("}");
        out.println();

        out.print("set the enabledNotificationsList to {");
        for (int i=0; i< enabled.length; i++) {
            out.format("\"%s\"", enabled[i]);
            if (i+1< enabled.length) {
                out.print(",");
            }
        }
        out.print("}");
        out.println();

        out.format("register as application \"%s\"" +
            " all notifications allNotificationsList" +
            " default notifications enabledNotificationsList", appName);
        out.println();

        out.println("end tell");
        out.flush();

        eval(buff);
    }

    public void notifyGrowlOf(final String notification, final String title, final String description) {
        assert notification != null;
        assert title != null;
        assert description != null;
        
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);

        out.format("tell application \"%s\"", GROWL_HELPER_APP).println();

        out.format("notify with name \"%s\" title \"%s\" description \"%s\" application name \"%s\"",
            notification, title, description, appName);
        out.println();
        
        out.println("end tell");
        out.flush();

        eval(buff);
    }

    private Object eval(final StringWriter buff) {
        assert buff != null;

        log.trace("Evaluating script:\n{}", buff);

        Object result = null;
        try {
            result = engine.eval(buff.toString());
            log.trace("Eval result: {}", result);
        }
        catch (ScriptException e) {
            log.trace("Failed to evaluate script: " + buff, e);
        }

        return result;
    }
}