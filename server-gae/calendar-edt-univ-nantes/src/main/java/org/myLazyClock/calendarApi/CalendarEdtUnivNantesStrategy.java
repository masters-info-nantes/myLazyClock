package org.myLazyClock.calendarApi;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarEdtUnivNantesStrategy implements CalendarStrategy {

    public static final int ID = 2;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar edt univ-nantes";
    }
}
