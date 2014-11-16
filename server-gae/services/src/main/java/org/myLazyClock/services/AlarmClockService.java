package org.myLazyClock.services;

import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;

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

    private AlarmClock findOne(Long alarmClockId) throws NotFoundMyLazyClockException {
        if (alarmClockId == null)
            throw new NotFoundMyLazyClockException();
        AlarmClock alarmClock = AlarmClockRepository.getInstance().findOne(alarmClockId);
        if(alarmClock == null)
            throw new NotFoundMyLazyClockException();
        return alarmClock;
    }

    public Collection<AlarmClock> findAll(String userId) {
        return AlarmClockRepository.getInstance().findAllByUserId(userId);
    }

    public AlarmClock item(String alarmClockId) throws NotFoundMyLazyClockException {
        return findOne(Long.parseLong(alarmClockId));
    }

    public AlarmClock link(AlarmClock alarmClock, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne(alarmClock.getId());
        if(a.getUser() != null)
            throw new ForbiddenMyLazyClockException();
        a.setUser(userId);
        a.setAddress(alarmClock.getAddress());
        a.setName(alarmClock.getName());
        a.setPreparationTime(alarmClock.getPreparationTime());
        a.setColor(alarmClock.getColor());
        return AlarmClockRepository.getInstance().save(a);
    }

    public AlarmClock unlink(String alarmClockId, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne(Long.parseLong(alarmClockId));
        if(!a.getUser().equals(userId))
            throw new ForbiddenMyLazyClockException();
        AlarmClock b = new AlarmClock();
        b.setId(a.getId());
        AlarmClockRepository.getInstance().delete(a);
        return AlarmClockRepository.getInstance().save(b);
    }

    public AlarmClock update(AlarmClock alarmClock, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne(alarmClock.getId());
        if(!a.getUser().equals(userId))
            throw new ForbiddenMyLazyClockException();
        a.setAddress(alarmClock.getAddress());
        a.setName(alarmClock.getName());
        a.setPreparationTime(alarmClock.getPreparationTime());
        a.setColor(alarmClock.getColor());
        return AlarmClockRepository.getInstance().save(a);
    }

    public AlarmClock generate() {
        AlarmClock alarmClock = new AlarmClock();
        return AlarmClockRepository.getInstance().save(alarmClock);
    }
}
