package org.myLazyClock.calendarApi;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarManualStrategy implements CalendarStrategy {

    public static final int ID = 1;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar Manual";
    }
}
