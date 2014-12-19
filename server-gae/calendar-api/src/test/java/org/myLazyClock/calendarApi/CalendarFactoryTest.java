package org.myLazyClock.calendarApi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalendarFactoryTest {

    private CalendarFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = CalendarFactory.getInstance();
    }

    @Test(expected = NullPointerException.class)
    public void testGetCalendarStrategy() {
        CalendarStrategy strategy = factory.get(-1);
        Assert.assertNull(strategy);
    }

}