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

import org.sonatype.grrrowl.impl.NativeGrowl;
import org.sonatype.grrrowl.impl.NullGrowl;

/**
 * Provides an interface to <a href="http://growl.info">Growl</a>.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class GrowlFactory
{
    //
    // TODO: Consider adding applescript support a fallback if jna is missing:
    //       http://growl.info/documentation/applescript-support.php
    //       Using javax.script muck to invoke.
    //
    
    public static Growl create(String appName) {
        try {
            return new NativeGrowl(appName);
        }
        catch (Throwable t) {
            return new NullGrowl();
        }
    }
}