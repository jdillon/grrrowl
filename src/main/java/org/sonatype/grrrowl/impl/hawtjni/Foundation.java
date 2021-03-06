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

package org.sonatype.grrrowl.impl.hawtjni;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.sonatype.grrrowl.impl.hawtjni.FoundationLibrary.*;


/**
 * Implementation of elements from the Cocoa Foundation library required to Growl.
 *
 * @author spleaner
 * @since 1.0
 */
public class Foundation
{
    private static final Logger log = LoggerFactory.getLogger(Foundation.class);

    private Foundation() {}

    /**
     * Get the ID of the NSClass with className
     */
    public static ID getClass(final String className) {
        log.trace("calling objc_getClass({})", className);
        return objc_getClass(className);
    }

    public static Selector createSelector(final String s) {
        return sel_registerName(s).initName(s);
    }

    public static ID invoke(final ID id, final Selector selector) {
        return objc_msgSend(id, selector);
    }
    public static ID invoke(ID id, Selector selector, ID arg1) {
        return objc_msgSend(id, selector, arg1);
    }

    public static ID invoke(ID id, Selector selector, ID arg1, ID arg2) {
        return objc_msgSend(id, selector, arg1, arg2);
    }

    public static ID invoke(ID id, Selector selector, ID arg1, ID arg2, ID arg3, boolean arg4) {
        return objc_msgSend(id, selector, arg1, arg2, arg3, arg4);
    }
    
    
    /**
     * Return a CFString as an ID, toll-free bridged to NSString.
     *
     * Note that the returned string must be freed with {@link #cfRelease(ID)}.
     */
    public static ID cfString(final String s) {
        // Use a byte[] rather than letting jna do the String -> char* marshaling itself.
        // Turns out about 10% quicker for long strings.
        try {
            byte[] utf16Bytes = s.getBytes("UTF-16LE");
            return CFStringCreateWithBytes(null, utf16Bytes,
                utf16Bytes.length, 0x14000100, (byte) 0); /* kTextEncodingUnicodeDefault + kUnicodeUTF16LEFormat */
        }
        catch (UnsupportedEncodingException x) {
            throw new RuntimeException(x);
        }
    }

    public static void cfRetain(final ID id) {
        CFRetain(id);
    }

    public static void cfRelease(final ID id) {
        CFRelease(id);
    }

    public static void load() {}

}
