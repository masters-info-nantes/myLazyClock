package org.myLazyClock.model.repository;

import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.Calendar;

import java.util.Collection;

/**
 * Created on 26/11/14.
 *
 * @author dralagen
 */
public class CalendarRepository {

    private static CalendarRepository repo = null;

    private CalendarRepository() {}

    public static CalendarRepository getInstance() {
        if (repo == null) {
            repo = new CalendarRepository();
        }
        return repo;
    }

    public Collection<Calendar> findAll(AlarmClock alarm) {

        return alarm.getCalendars();
    }
}
