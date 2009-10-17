package org.sonatype.grrrowl;

import org.junit.Test;

/**
 * Tests for {@link Growl}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class TestGrowl
{
    @Test
    public void testGrowl() throws Exception {
        Growl growl = new Growl("Test Growl");
        String[] notifications = {
            "foo",
            "bar"
        };
        growl.setAllowedNotifications(notifications);
        growl.setDefaultNotifications(notifications);
        growl.register();

        growl.notifyGrowlOf("foo", "Test Foo", "This is a test of the 'foo' notification.");
    }
}
