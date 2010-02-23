Description
-----------

Support for Growl Notifications.

Uses [HawtJNI](http://hawtjni.fusesource.org/) to generate a native library to access Growl.

Updated to support Growling from AppleScript if the native library is not installed, else defaulting
to a version that simply swallows notifications (for platforms w/o Growl support).

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Features
--------

* Native Growl support using [HawtJNI](http://hawtjni.fusesource.org/)
* AppleScript Growl when JNA is not present
* Fallback to null implementation on unsupported platforms

Example Usage
-------------

    import org.sonatype.grrrowl.Growler;
    ...
    Growler growler = new Growler("My Application")
        .add("Notification1", "Notification2")
        .enableAll()
    ....
    growler.growl("Notification1", "Title for Notification1", "Description for Notification1");

or using enums:

    import org.sonatype.grrrowl.Growler;
    
    enum Notifications {
        Notification1,
        Notification2 
    }
    ...
    Growler growler = new Growler("My Application")
        .add(Notifications.class)
        .enableAll();
    ....
    growler.growl(Notifications.Notification1, "Title for Notification1", "Description for Notification1");

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2+
* [Java](http://java.sun.com/) 5+
* [Xcode](http://developer.apple.com/technology/xcode.html) 3.2+

Check-out and build:

    git clone git://github.com/jdillon/grrrowl.git
    cd grrrowl
    mvn install
