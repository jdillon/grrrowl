Description
-----------

Support for Growl Notifications.

JNA code comes from [Intellij IDEA Community Edition](http://www.jetbrains.org/display/IJOS/Home)
and has been massaged slightly.

Updated to support Growling from AppleScript if the JNA library is not installed, else defaulting
to a version that simply swallows notifications (for platforms w/o Growl support).

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Features
--------

* Native Growl support using [JNA](https://jna.dev.java.net)
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

* [Maven](http://maven.apache.org) 2.x
* [Java](http://java.sun.com/) 5

Check-out and build:

    git clone git://github.com/jdillon/grrrowl.git
    cd grrrowl
    mvn install
