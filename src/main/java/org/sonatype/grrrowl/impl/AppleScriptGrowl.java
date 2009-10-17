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
 * Provides {@link Growl} support via AppleScript.
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
    
    private final ScriptEngineManager manager;

    private final ScriptEngine engine;

    private final String appName;

    private String[] notifications;

    private String[] allowed;

    public AppleScriptGrowl(final String appName) {
        assert appName != null;

        this.appName = appName;

        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("AppleScript");
        if (engine == null) {
            throw new UnsupportedOperationException("AppleScript engine is not available");
        }

        // TODO: Check if we can send scripts to apps not installed
    }

    public void setAllowedNotifications(final String... notifications) {
        assert notifications != null;
        this.notifications = notifications;
    }

    public void setDefaultNotifications(final String... notifications) {
        assert notifications != null;
        this.allowed = notifications;
    }

    public void register() {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);

        out.format("tell application '%s'", GROWL_HELPER_APP).println();

        out.print("set the allNotificationsList to {");
        for (int i=0; i<notifications.length; i++) {
            out.format("'%s'", notifications[i]);
            if (i+1<notifications.length) {
                out.print(",");
            }
        }
        out.print("}");
        out.println();

        out.print("set the enabledNotificationsList to {");
        for (int i=0; i<allowed.length; i++) {
            out.format("'%s'", allowed[i]);
            if (i+1<allowed.length) {
                out.print(",");
            }
        }
        out.print("}");
        out.println();

        out.format("register as application '%s'", appName);
        out.print(" all notifications allNotificationsList");
        out.print(" default notifications enabledNotificationsList");
        out.println();

        out.println("end tell");
        out.flush();

        log.trace("Register script:\n{}", buff);

        eval(buff);
    }

    public void notifyGrowlOf(final String notification, final String title, final String description) {
        StringWriter buff = new StringWriter();
        PrintWriter out = new PrintWriter(buff);

        out.format("tell application '%s'", GROWL_HELPER_APP).println();
        out.format(" notify with name '%s'", notification);
        out.format(" title '%s'", title);
        out.format(" description '%s'", description);
        out.format(" application name '%s'", appName).println();
        out.println();
        out.println("end tell");
        out.flush();

        log.trace("Notify script:\n{}", buff);

        eval(buff);
    }

    private void eval(final StringWriter buff) {
        assert buff != null;
        
        try {
            engine.eval(buff.toString());
        }
        catch (ScriptException e) {
            log.trace("Failed to evaluate script: " + buff, e);
        }
    }
}