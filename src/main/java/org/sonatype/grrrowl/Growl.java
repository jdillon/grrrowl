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

package org.sonatype.grrrowl;

/**
 * Provides an interface to <a href="http://growl.info">Growl</a>.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public interface Growl
{
    /**
     * Set the list of notifications which are supported.
     *
     * @param notifications
     */
    void setAllowedNotifications(String... notifications);

    /**
     * Set the list of notifications which are enabled.
     *
     * @param notifications
     */
    void setEnabledNotifications(String... notifications);

    /**
     * @since 1.1
     */
    boolean isGrowlRunning();

    /**
     * Register.
     */
    void register();

    /**
     * Send a Growl notification.
     * 
     * @param notification
     * @param title
     * @param description
     */
    void notifyGrowlOf(String notification, String title, String description);
}