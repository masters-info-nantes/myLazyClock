package org.myLazyClock.calendarApi;

import org.junit.Assert;
import org.junit.Test;

public class CalendarIcsStrategyTest {

    @Test
    public void testFactory() throws Exception {

        CalendarFactory factory = CalendarFactory.getInstance();

        CalendarStrategy strategy = factory.get(CalendarIcsStrategy.ID);

        Assert.assertNotNull(strategy);

        Assert.assertTrue(strategy instanceof CalendarIcsStrategy);

    }
}