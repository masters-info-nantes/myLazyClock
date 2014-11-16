package org.myLazyClock.calendarApi;

import java.util.Calendar;

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
    
    @Override
    public CalendarEvent getFirstEvent(String url, Calendar day) throws EventNotFoundException {
        if (day == null) {
            throw new EventNotFoundException();
        }

        CalendarEvent returnEvent = new CalendarEvent();

        returnEvent.setBeginDate(day.getTime());

        return returnEvent;
    }
}
