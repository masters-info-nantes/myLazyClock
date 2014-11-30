package org.myLazyClock.calendarApi;

import org.junit.Assert;
import org.junit.Test;

public class CalendarSmartEdtStrategyTest {

    @Test
    public void testFactory() throws Exception {

        CalendarFactory factory = CalendarFactory.getInstance();

        CalendarStrategy strategy = factory.get(CalendarSmartEdtStrategy.ID);

        Assert.assertNotNull(strategy);

        Assert.assertTrue(strategy instanceof CalendarSmartEdtStrategy);

    }
}