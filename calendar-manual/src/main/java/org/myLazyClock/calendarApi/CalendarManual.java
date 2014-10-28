package org.myLazyClock.calendarApi;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarManual implements CalendarStrategy {

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public String getName() {
        return "Calendar Manual";
    }
}
