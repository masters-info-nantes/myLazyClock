/*
 * myLazyClock
 *
 * Copyright (C) 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.myLazyClock.services;

import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;

import java.util.Collection;

/**
 * Created on 22/10/14.
 *
 * @author Maxime
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

    private AlarmClock findOne(String alarmClockId) throws NotFoundMyLazyClockException {
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
        return findOne(alarmClockId);
    }

    public AlarmClock link(AlarmClock alarmClock, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne(String.valueOf(alarmClock.getId()));
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
        AlarmClock a = findOne(alarmClockId);
        if(!a.getUser().equals(userId))
            throw new ForbiddenMyLazyClockException();
        AlarmClock b = new AlarmClock();
        b.setId(a.getId());
        AlarmClockRepository.getInstance().delete(a);
        return AlarmClockRepository.getInstance().save(b);
    }

    public AlarmClock update(AlarmClock alarmClock, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne(String.valueOf(alarmClock.getId()));
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
