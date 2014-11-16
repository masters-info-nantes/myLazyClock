package org.myLazyClock.calendarApi;

import java.util.Calendar;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarGoogleStrategy implements CalendarStrategy {

    public static final int ID = 3;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar Google";
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
