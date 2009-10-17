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

import com.sun.jna.Native;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * ???
 *
 * @author spleaner
 *
 * @since 1.0
 */
public class Foundation
{
    private static final Logger log = LoggerFactory.getLogger(Foundation.class);

    private static final FoundationLibrary foundationLibrary;

    static {
        // Set JNA to convert java.lang.String to char* using UTF-8, and match that with
        // the way we tell CF to interpret our char*
        // May be removed if we use toStringViaUTF16
        System.setProperty("jna.encoding", "UTF8");

        foundationLibrary = (FoundationLibrary) Native.loadLibrary("Foundation", FoundationLibrary.class);
    }

    private Foundation() {}

    /**
     * Get the ID of the NSClass with className
     */
    public static ID getClass(String className) {
        log.trace("calling objc_getClass({})", className);
        return foundationLibrary.objc_getClass(className);
    }

    public static Selector createSelector(String s) {
        return foundationLibrary.sel_registerName(s).initName(s);
    }

    public static ID invoke(final ID id, final Selector selector, Object... args) {
        return foundationLibrary.objc_msgSend(id, selector, args);
    }

    /**
     * Return a CFString as an ID, toll-free bridged to NSString.
     * <p/>
     * Note that the returned string must be freed with {@link #cfRelease(ID)}.
     */
    public static ID cfString(String s) {
        // Use a byte[] rather than letting jna do the String -> char* marshalling itself.
        // Turns out about 10% quicker for long strings.
        try {
            byte[] utf16Bytes = s.getBytes("UTF-16LE");
            return foundationLibrary.CFStringCreateWithBytes(null, utf16Bytes,
                utf16Bytes.length, 0x14000100, (byte) 0); /* kTextEncodingUnicodeDefault + kUnicodeUTF16LEFormat */
        }
        catch (UnsupportedEncodingException x) {
            throw new RuntimeException(x);
        }
    }

    public static void cfRetain(final ID id) {
        foundationLibrary.CFRetain(id);
    }

    public static void cfRelease(final ID id) {
        foundationLibrary.CFRelease(id);
    }

    public static void load() {}
}
