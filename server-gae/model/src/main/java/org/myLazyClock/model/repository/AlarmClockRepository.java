package org.myLazyClock.model.repository;

/**
 * Created on 22/10/14.
 *
 * @author Maxime, Dralagen
 */

import org.myLazyClock.model.model.AlarmClock;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class AlarmClockRepository {

    private static AlarmClockRepository repo = null;

    private AlarmClockRepository() {
    }

    public static synchronized AlarmClockRepository getInstance() {
        if (null == repo) {
            repo = new AlarmClockRepository();
        }
        return repo;
    }

    public Collection<AlarmClock> findAll() {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery(AlarmClock.class);

        List<AlarmClock> all;

        try {
            all = (List<AlarmClock>) q.execute();
        } finally {
            q.closeAll();
        }

        return all;
    }

    public Collection<AlarmClock> findAllByUserId(String userId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery(AlarmClock.class);
        q.setFilter("user == userId");
        q.declareParameters("String userId");
        List<AlarmClock> all;

        try {
            all = (List<AlarmClock>) q.execute(userId);
        } finally {
            q.closeAll();
        }

        return all;
    }

    public AlarmClock findOne(String id) {
        return findOne(Long.decode(id));
    }

    public AlarmClock findOne(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        AlarmClock one = pm.getObjectById(AlarmClock.class, id);

        return one;
    }

    public AlarmClock save(AlarmClock alarmClock) {

        PersistenceManager pm = null;

        if (alarmClock.getId() == null) {
            pm = PMF.get().getPersistenceManager();
        }
        else {
            pm = JDOHelper.getPersistenceManager(alarmClock);
        }

        try {
            pm.makePersistent(alarmClock);
        } finally {
            pm.close();
        }

        return alarmClock;
    }

    public void delete(AlarmClock alarmClock) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        pm.deletePersistent(alarmClock.getId());
    }

}
