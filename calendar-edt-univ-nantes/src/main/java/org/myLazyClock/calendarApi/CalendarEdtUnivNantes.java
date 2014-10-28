package org.myLazyClock.calendarApi;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarEdtUnivNantes implements CalendarStrategy {

    @Override
    public Integer getId() {
        return 2;
    }

    @Override
    public String getName() {
        return "Calendar edt univ-nantes";
    }
}
