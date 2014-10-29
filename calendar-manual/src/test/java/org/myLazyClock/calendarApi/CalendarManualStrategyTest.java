package org.myLazyClock.calendarApi;

import org.junit.Assert;
import org.junit.Test;

public class CalendarManualStrategyTest {

    @Test
    public void testFactory() throws Exception {

        CalendarFactory factory = CalendarFactory.getInstance();

        CalendarStrategy strategy = factory.get(CalendarManualStrategy.ID);

        Assert.assertNotNull(strategy);

        Assert.assertTrue(strategy instanceof CalendarManualStrategy);

    }
}