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

package org.sonatype.grrrowl.impl.jna;

import com.sun.jna.Library;
import com.sun.jna.Structure;

/**
 * Representation of elements from the Cocoa Foundation library required to Growl.
 *
 * @author spleaner
 * @since 1.0
 */
public interface FoundationLibrary
    extends Library
{
    void NSLog(ID pString, Object thing);

    ID CFStringCreateWithCString(ID allocator, String string, int encoding);

    ID CFStringCreateWithBytes(ID allocator, byte[] bytes, int byteCount, int encoding, byte isExternalRepresentation);

    String CFStringGetCStringPtr(ID string, int encoding);

    byte CFStringGetCString(ID theString, byte[] buffer, int bufferSize, int encoding);

    int CFStringGetLength(ID theString);

    void CFRetain(ID cfTypeRef);

    void CFRelease(ID cfTypeRef);

    int CFGetRetainCount(ID cfTypeRef);

    ID objc_getClass(String className);

    ID class_createInstance(ID pClass, int extraBytes);

    Selector sel_registerName(String selectorName);

    ID objc_msgSend(ID receiver, Selector selector, Object... args);

    Structure objc_msgSend_stret(ID receiver, Selector selector, Object... args);
}
