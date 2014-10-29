package org.myLazyClock.calendarApi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalendarEdtUnivNantesTest {

    @Test
    public void testFactory() throws Exception {

        CalendarFactory factory = CalendarFactory.getInstance();

        CalendarStrategy strategy = factory.get(CalendarEdtUnivNantes.ID);

        Assert.assertNotNull(strategy);

        Assert.assertTrue(strategy instanceof CalendarEdtUnivNantes);

    }
}