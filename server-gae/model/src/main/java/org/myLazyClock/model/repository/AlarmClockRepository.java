package org.myLazyClock.model.repository;

/**
 * Created by Maxime on 22/10/14.
 */

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import org.myLazyClock.model.model.AlarmClock;

import java.util.Collection;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AlarmClockRepository {

    private static AlarmClockRepository repo = null;

    static {
        ObjectifyService.register(AlarmClock.class);
    }

    private AlarmClockRepository() {}

    public static synchronized AlarmClockRepository getInstance() {
        if (null == repo) {
            repo = new AlarmClockRepository();
        }
        return repo;
    }

    public Collection<AlarmClock> findAll() {
        List<AlarmClock> all = ofy().load().type(AlarmClock.class).list();
        return all;
    }

    public Collection<AlarmClock> findAllByUserId(String userId) {
        Query<AlarmClock> all = ofy().load().type(AlarmClock.class).filter("user", userId);
        return all.list();
    }

    public AlarmClock findOne(String id) {
        return findOne(Long.valueOf(id));
    }

    public AlarmClock findOne(Long id) {
        AlarmClock one = ofy().load().type(AlarmClock.class).id(id).now();
        return one;
    }

    public AlarmClock save(AlarmClock alarmClock) {
        ofy().save().entity(alarmClock).now();
        return alarmClock;
    }

}
