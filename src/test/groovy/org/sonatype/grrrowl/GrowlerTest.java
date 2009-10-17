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

import org.junit.Test;

/**
 * Tests for {@link Growler}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GrowlerTest
    extends GrowlTestSupport
{
    @Test
    public void testGrowl() throws Exception {
        Growler growl = new Growler(getClass().getName() + "1", "foo");
        growl.growl("foo", "Test " + getClass().getSimpleName() + "1", getClass().getSimpleName());
    }

    @Test
    public void testGrowlNative() throws Exception {
        Growler growl = new Growler(getClass().getName() + "2", "foo");
        growl.growl("foo", "Test " + getClass().getSimpleName() + "2", getClass().getSimpleName());
    }
}