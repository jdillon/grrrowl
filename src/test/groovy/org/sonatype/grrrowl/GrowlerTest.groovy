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

package org.sonatype.grrrowl

import org.junit.Test

/**
 * Tests for {@link Growler}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class GrowlerTest
    extends GrowlTestSupport
{
    @Test
    void testGrowl() {
        Growler growl = new Growler(getClass().name, "foo")
        growl.growl("foo", getClass().simpleName, getClass().simpleName)
    }

    @Test
    void testGrowlWithEnum() {
        Growler growl = new Growler(getClass().name, TestNotifications.FOO)
        growl.growl(TestNotifications.FOO, getClass().simpleName, getClass().simpleName)
    }
}

enum TestNotifications
{
    FOO, BAR
}