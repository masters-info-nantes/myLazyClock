package org.myLazyClock.model.repository;

import com.google.appengine.api.datastore.Key;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.Calendar;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import java.util.Collection;
import java.util.List;

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

    public Calendar add(Calendar calendar, AlarmClock alarm) {
        List<Calendar> calendars = alarm.getCalendars();
        calendars.add(calendar);

        AlarmClockRepository.getInstance().save(alarm);

        return calendar;
    }

    public Calendar save(Calendar calendar) {
        PersistenceManager pm;

        if (calendar.getKey() == null) {
            pm = PMF.get().getPersistenceManager();
        } else {
            pm = JDOHelper.getPersistenceManager(calendar);
        }

        try {
            pm.makePersistent(calendar);
        } finally {
            pm.close();
        }

        return calendar;
    }

    public Calendar findOne(Key calendarKey) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        return pm.getObjectById(Calendar.class, calendarKey);
    }

    public void delete(Calendar calendar) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.deletePersistent(calendar);
        } finally {
            pm.close();
        }
    }
}
