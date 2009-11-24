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

import org.sonatype.grrrowl.Growl;

/**
 * A null/do-nothing {@link Growl}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class NullGrowl
    implements Growl
{
    public NullGrowl(final String ignore) {
        // Nothing
    }

    public NullGrowl() {
        // Nothing
    }

    public void register() {
        // nothing
    }

    public void notifyGrowlOf(final String notification, final String title, final String description) {
        // nothing
    }

    public void setAllowedNotifications(final String... notifications) {
        // nothing
    }

    public void setEnabledNotifications(final String... notifications) {
        // nothing
    }
}