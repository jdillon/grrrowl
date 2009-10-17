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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.grrrowl.Growl;

/**
 * Native (via <a href="https://jna.dev.java.net/">JNA</a>) implementation of {@link Growl}.
 *
 * @author spleaner
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class NativeGrowl
    implements Growl
{
    private static final String GROWL_APPLICATION_REGISTRATION_NOTIFICATION = "GrowlApplicationRegistrationNotification";
    private static final String GROWL_APP_NAME = "ApplicationName";
    private static final String GROWL_APP_ICON = "ApplicationIcon";
    private static final String GROWL_DEFAULT_NOTIFICATIONS = "DefaultNotifications";
    private static final String GROWL_ALL_NOTIFICATIONS = "AllNotifications";
    private static final String GROWL_NOTIFICATION_NAME = "NotificationName";
    private static final String GROWL_NOTIFICATION_TITLE = "NotificationTitle";
    private static final String GROWL_NOTIFICATION_DESCRIPTION = "NotificationDescription";
    private static final String GROWL_NOTIFICATION = "GrowlNotification";

    private static final String NSARRAY = "NSArray";
    private static final String NSDICTIONARY = "NSDictionary";
    private static final String NSAPPLICATION = "NSApplication";
    private static final String NSMUTABLE_ARRAY = "NSMutableArray";
    private static final String NSAUTORELEASE_POOL = "NSAutoreleasePool";
    private static final String NSDISTRIBUTED_NOTIFICATION_CENTER = "NSDistributedNotificationCenter";

    private static final Logger log = LoggerFactory.getLogger(NativeGrowl.class);

    private String myProductName;
    private String[] myAllNotifications;
    private String[] myDefaultNotification;

    public NativeGrowl(final String productName) {
        assert productName != null;
        myProductName = productName;
    }

    public void register() {
        final ID autoReleasePool = createAutoReleasePool();
        final ID applicationIcon = getApplicationIcon();
        final ID defaultNotifications = fillArray(myDefaultNotification);
        final ID allNotifications = fillArray(myAllNotifications);

        final ID userDict = createDict(new String[]{
                GROWL_APP_NAME, GROWL_APP_ICON, GROWL_DEFAULT_NOTIFICATIONS, GROWL_ALL_NOTIFICATIONS
            },
            new Object[]{
                myProductName, applicationIcon, defaultNotifications, allNotifications
            });

        final ID center = invoke(NSDISTRIBUTED_NOTIFICATION_CENTER, "defaultCenter");
        final Object notificationName = Foundation.cfString(GROWL_APPLICATION_REGISTRATION_NOTIFICATION).toNative();
        invoke(center, "postNotificationName:object:userInfo:deliverImmediately:", notificationName, null, userDict, true);
        invoke(autoReleasePool, "release");
    }

    public void notifyGrowlOf(final String notification, final String title, final String description) {
        log.debug("Notifying growl of: {} {} {}", new Object[] {notification, title, description});

        final ID autoReleasePool = createAutoReleasePool();

        final ID dict = createDict(new String[]{
                GROWL_NOTIFICATION_NAME, GROWL_NOTIFICATION_TITLE, GROWL_NOTIFICATION_DESCRIPTION, GROWL_APP_NAME
            },
            new Object[]{
                notification, title, description, myProductName
            });

        final ID center = invoke(NSDISTRIBUTED_NOTIFICATION_CENTER, "defaultCenter");
        final Object notificationName = Foundation.cfString(GROWL_NOTIFICATION).toNative();

        invoke(center, "postNotificationName:object:userInfo:deliverImmediately:", notificationName, null, dict, true);
        invoke(autoReleasePool, "release");
    }

    public void setAllowedNotifications(final String... notifications) {
        myAllNotifications = notifications;
    }

    public void setDefaultNotifications(final String... notifications) {
        myDefaultNotification = notifications;
    }

    private static ID createAutoReleasePool() {
        return invoke(NSAUTORELEASE_POOL, "new");
    }

    private static ID fillArray(final Object... a) {
        final ID result = invoke(NSMUTABLE_ARRAY, "array");
        for (Object s : a) {
            invoke(result, "addObject:", convertType(s));
        }

        return result;
    }

    private static ID createDict(final String[] keys, final Object[] values) {
        assert keys != null;
        assert values != null;
        final ID nsKeys = invoke(NSARRAY, "arrayWithObjects:", convertTypes(keys));
        final ID nsData = invoke(NSARRAY, "arrayWithObjects:", convertTypes(values));

        return invoke(NSDICTIONARY, "dictionaryWithObjects:forKeys:", nsData, nsKeys);
    }

    private static Object convertType(final Object o) {
        if (o instanceof ID) {
            return o;
        }
        else if (o instanceof String) {
            return Foundation.cfString((String) o).toNative();
        }
        else {
            throw new IllegalArgumentException("Unsupported type: " + o.getClass());
        }
    }

    private static Object[] convertTypes(final Object... v) {
        assert v != null;
        final Object[] result = new Object[v.length];
        for (int i = 0; i < v.length; i++) {
            result[i] = convertType(v[i]);
        }
        return result;
    }

    //
    // TODO: Allow the icon to be given
    //

    private static ID getApplicationIcon() {
        final ID sharedApp = invoke(NSAPPLICATION, "sharedApplication");
        final ID nsImage = invoke(sharedApp, "applicationIconImage");
        return invoke(nsImage, "TIFFRepresentation");
    }

    private static ID invoke(final String className, final String selector, final Object... args) {
        assert className != null;
        assert selector != null;
        return invoke(Foundation.getClass(className), selector, args);
    }

    private static ID invoke(final ID id, final String selector, final Object... args) {
        assert id != null;
        assert selector != null;
        return invoke(id, Foundation.createSelector(selector), args);
    }

    private static ID invoke(final ID id, final Selector selector, final Object... args) {
        assert id != null;
        assert selector != null;
        return Foundation.invoke(id, selector, args);
    }
}