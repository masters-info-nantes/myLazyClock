package org.myLazyClock.calendarApi;

import org.junit.Assert;
import org.junit.Test;

public class CalendarManualTest {

    @Test
    public void testFactory() throws Exception {

        CalendarFactory factory = CalendarFactory.getInstance();

        CalendarStrategy strategy = factory.get(CalendarManual.ID);

        Assert.assertNotNull(strategy);

        Assert.assertTrue(strategy instanceof CalendarManual);

    }
}