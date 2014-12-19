package org.myLazyClock.calendarApi;

import org.junit.Assert;
import org.junit.Test;

public class CalendarGoogleStrategyTest {

    @Test
    public void testFactory() throws Exception {

        CalendarFactory factory = CalendarFactory.getInstance();

        CalendarStrategy strategy = factory.get(CalendarGoogleStrategy.ID);

        Assert.assertNotNull(strategy);

        Assert.assertTrue(strategy instanceof CalendarGoogleStrategy);

    }
}