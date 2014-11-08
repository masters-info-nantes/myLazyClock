package org.myLazyClock.services;

import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.repository.AlarmClockRepository;

import java.util.Collection;

/**
 * Created by Maxime on 22/10/14.
 */
public class AlarmClockService {

    private static AlarmClockService service = null;

    private AlarmClockService() {}

    public static synchronized AlarmClockService getInstance() {
        if (null == service) {
            service = new AlarmClockService();
        }
        return service;
    }


    public Collection<AlarmClock> findAll() {
        return AlarmClockRepository.getInstance().findAll();
    }

    public Collection<AlarmClock> findAll(String userId) {
        return AlarmClockRepository.getInstance().findAllByUserId(userId);
    }

    public AlarmClock item(String alarmClockId, String userId) {
        return AlarmClockRepository.getInstance().findOne(alarmClockId);
    }

    public AlarmClock link(String alarmClockId, String userId) {
        AlarmClock alarmClock = AlarmClockRepository.getInstance().findOne(alarmClockId);
        alarmClock.setUser(userId);
        return AlarmClockRepository.getInstance().save(alarmClock);
    }

    public AlarmClock generate() {
        AlarmClock alarmClock = new AlarmClock();
        return AlarmClockRepository.getInstance().save(alarmClock);
    }
}
