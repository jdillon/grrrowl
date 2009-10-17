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
import org.sonatype.grrrowl.impl.AppleScriptGrowl;
import org.sonatype.grrrowl.impl.NativeGrowl;
import org.sonatype.grrrowl.impl.NullGrowl;

/**
 * Tests for {@link org.sonatype.grrrowl.Growler}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GrowlerFactoryTest
    extends GrowlTestSupport
{
    @Test
    public void testGrowlNull() throws Exception {
        System.setProperty(GrowlFactory.TYPE, NullGrowl.class.getName());
        Growler growl = new Growler(NullGrowl.class.getName(), "foo");
        growl.growl("foo", "Test Null", "null");
    }

    @Test
    public void testGrowlNative() throws Exception {
        System.setProperty(GrowlFactory.TYPE, NativeGrowl.class.getName());
        Growler growl = new Growler(NativeGrowl.class.getName(), "foo");
        growl.growl("foo", "Test Native", "native");
    }

    @Test
    public void testGrowlAppleScript() throws Exception {
        System.setProperty(GrowlFactory.TYPE, AppleScriptGrowl.class.getName());
        Growler growl = new Growler(AppleScriptGrowl.class.getName(), "foo");
        growl.growl("foo", "Test AppleScript", "as");
    }
}