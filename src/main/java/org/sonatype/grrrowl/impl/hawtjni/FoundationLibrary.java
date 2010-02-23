/*
 * Copyright (C) 2009 the original author or authors.
 * Copyright (C) 2010, Progress Software Corporation and/or its
 * subsidiaries or affiliates.  All rights reserved.
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

import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;
import org.fusesource.hawtjni.runtime.Library;

import static org.fusesource.hawtjni.runtime.ArgFlag.*;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

/**
 * Representation of elements from the Cocoa Foundation library required to Growl.
 *
 * @author <a href="hiramchirino.com">Hiram Chirino</a>
 */
@JniClass(conditional="defined(__APPLE__)")
public class FoundationLibrary {
    
    private static final Library LIBRARY = new Library("grrrowl", Foundation.class);    
    static {
        LIBRARY.load();
    }

    public static final void NSLog(ID pString, Object thing) {
        _NSLog(id(pString), ((Long)thing).longValue());
    }    
    @JniMethod(accessor="NSLog")
    public static native void _NSLog(
            @JniArg(cast="void *") long pString, 
            @JniArg(cast="void *") long thing);

    public static ID CFStringCreateWithCString(ID allocator, String string, int encoding) {
        return new ID(_CFStringCreateWithCString(id(allocator), string, encoding));
    }    
    @JniMethod(cast="CFStringRef", flags={POINTER_RETURN}, accessor="CFStringCreateWithCString")
    public static native long _CFStringCreateWithCString(
            @JniArg(cast="CFAllocatorRef", flags={POINTER_ARG}) long allocator, 
            @JniArg(cast="const char *") String string, 
            @JniArg(cast="CFStringEncoding") int encoding);

    public static ID CFStringCreateWithBytes(ID allocator, byte[] bytes, int byteCount, int encoding, byte isExternalRepresentation) {
        return new ID(_CFStringCreateWithBytes(id(allocator), bytes, byteCount, encoding, isExternalRepresentation));
    }
    
    private static long id(ID allocator) {
        if( allocator==null ) {
            return 0;
        }
        return allocator.longValue();
    }
    
    @JniMethod(cast="CFStringRef", flags={POINTER_RETURN}, accessor="CFStringCreateWithBytes")
    public static native long _CFStringCreateWithBytes(
            @JniArg(cast="CFAllocatorRef", flags={POINTER_ARG}) long allocator, 
            @JniArg(cast="const UInt8 *") byte[] bytes, 
            @JniArg(cast="CFIndex") int byteCount, 
            @JniArg(cast="CFStringEncoding") int encoding, 
            @JniArg(cast="Boolean") byte isExternalRepresentation);

    public static String CFStringGetCStringPtr(ID string, int encoding) {
        long cPtr = _CFStringGetCStringPtr(id(string), encoding);
        int len = strlen(cPtr);
        byte[] data = new byte[len];
        memmove(data, cPtr, len);
        return new String(data);        
    }
    
    public static native int strlen(@JniArg(cast="const char *") long string);
    public static native int memmove(
            @JniArg(cast="void *", flags={NO_OUT}) byte[] dest,
            @JniArg(cast="void *") long source,
            int count
            );
    
    @JniMethod(cast="const char *", accessor="CFStringGetCStringPtr")
    public static native long _CFStringGetCStringPtr(
            @JniArg(cast="CFStringRef", flags={POINTER_ARG}) long string, 
            @JniArg(cast="CFStringEncoding") int encoding);

    public static byte CFStringGetCString(ID theString, byte[] buffer, int bufferSize, int encoding) {
        return _CFStringGetCString(id(theString), buffer, bufferSize, encoding);
    }
    @JniMethod(cast="Boolean", accessor="CFStringGetCString")
    public static native byte _CFStringGetCString(
            @JniArg(cast="CFStringRef", flags={POINTER_ARG}) long theString, 
            @JniArg(cast="char *") byte[] buffer, 
            @JniArg(cast="CFIndex") int bufferSize, 
            @JniArg(cast="CFStringEncoding") int encoding);

    public static int CFStringGetLength(ID theString) {
        return _CFStringGetLength(id(theString));
    }
    @JniMethod(cast="CFIndex", accessor="CFStringGetLength")
    public static native int _CFStringGetLength(
            @JniArg(cast="CFStringRef", flags={POINTER_ARG}) long theString);

    public static void CFRetain(ID cfTypeRef) {
        _CFRetain(id(cfTypeRef));
    }
    @JniMethod(cast="CFIndex", accessor="CFRetain")
    public static native void _CFRetain(
            @JniArg(cast="CFTypeRef", flags={POINTER_ARG}) long cfTypeRef);

    public static void CFRelease(ID cfTypeRef) {
        _CFRelease(id(cfTypeRef));
    }
    @JniMethod(cast="CFIndex", accessor="CFRelease")
    public static native void _CFRelease(
            @JniArg(cast="CFTypeRef", flags={POINTER_ARG}) long cfTypeRef);

    public static int CFGetRetainCount(ID cfTypeRef) {
        return _CFGetRetainCount(id(cfTypeRef));
    }    
    @JniMethod(cast="CFIndex", accessor="CFGetRetainCount")
    public static native int _CFGetRetainCount(
            @JniArg(cast="CFTypeRef", flags={POINTER_ARG}) long cfTypeRef);

    public static ID objc_getClass(String className) {
        return new ID(_objc_getClass(className));
    }
    @JniMethod(cast="id", flags={POINTER_RETURN}, accessor="objc_getClass")
    public static final native long _objc_getClass(String className);

    public static Selector sel_registerName(String selectorName) {
        return new Selector(_sel_registerName(selectorName));
    }
    @JniMethod(cast="SEL", flags={POINTER_RETURN}, accessor="sel_registerName")
    public static final native long _sel_registerName(String selectorName);


    public static ID class_createInstance(ID pClass, int extraBytes) {
        return new ID(_class_createInstance(id(pClass), extraBytes));
    }
    @JniMethod(cast="id", flags={POINTER_RETURN}, accessor="class_createInstance")
    public static native long _class_createInstance(
            @JniArg(cast="Class", flags={POINTER_ARG})long pClass, 
            @JniArg(cast="size_t" )int extraBytes);
    
    public static ID objc_msgSend(ID receiver, Selector selector) {
        return new ID(_objc_msgSend(id(receiver), selector.longValue()));
    }
    @JniMethod(cast="id", flags={POINTER_RETURN}, accessor="objc_msgSend")
    public static final native long _objc_msgSend(
            @JniArg(cast="id", flags={POINTER_ARG})long id, 
            @JniArg(cast="SEL", flags={POINTER_ARG})long sel);


    public static ID objc_msgSend(ID receiver, Selector selector, ID arg1) {
        return new ID(_objc_msgSend(id(receiver), selector.longValue(), id(arg1)));
    }
    @JniMethod(cast="id", flags={POINTER_RETURN}, accessor="objc_msgSend")
    public static final native long _objc_msgSend(
            @JniArg(cast="id", flags={POINTER_ARG})long id, 
            @JniArg(cast="SEL", flags={POINTER_ARG})long sel,
            @JniArg(cast="id", flags={POINTER_ARG})long arg1);


    public static ID objc_msgSend(ID receiver, Selector selector, ID arg1, ID arg2) {
        return new ID(_objc_msgSend(id(receiver), selector.longValue(), id(arg1), id(arg2)));
    }
    @JniMethod(cast="id", flags={POINTER_RETURN}, accessor="objc_msgSend")
    public static final native long _objc_msgSend(
            @JniArg(cast="id", flags={POINTER_ARG})long id, 
            @JniArg(cast="SEL", flags={POINTER_ARG})long sel,
            @JniArg(cast="id", flags={POINTER_ARG})long arg1,
            @JniArg(cast="id", flags={POINTER_ARG})long arg2
            );

    
    public static ID objc_msgSend(ID receiver, Selector selector, ID arg1, ID arg2, ID arg3, boolean arg4) {
        return new ID(_objc_msgSend(id(receiver), selector.longValue(), id(arg1), id(arg2), id(arg3), arg4));
    }
    @JniMethod(cast="id", flags={POINTER_RETURN}, accessor="objc_msgSend")
    public static final native long _objc_msgSend(
            @JniArg(cast="id", flags={POINTER_ARG})long id, 
            @JniArg(cast="SEL", flags={POINTER_ARG})long sel,
            @JniArg(cast="id", flags={POINTER_ARG})long arg1,
            @JniArg(cast="id", flags={POINTER_ARG})long arg2,
            @JniArg(cast="id", flags={POINTER_ARG})long arg3,
            boolean arg4
            );


}
