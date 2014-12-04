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
import org.myLazyClock.services.bean.AlarmClockBean;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;

import java.util.ArrayList;
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

    public AlarmClockBean findOne(String alarmClockId) throws NotFoundMyLazyClockException {
        AlarmClockBean bean = new AlarmClockBean();
        bean.fromEntity(findOne_(alarmClockId));
        return bean;
    }

    private AlarmClock findOne_(String id) throws NotFoundMyLazyClockException {

        if (id == null) {
            throw new NotFoundMyLazyClockException();
        }

        AlarmClock alarmClock = AlarmClockRepository.getInstance().findOne(Long.decode(id));

        if (alarmClock == null) {
            throw new NotFoundMyLazyClockException();
        }

        return alarmClock;
    }


    public Collection<AlarmClockBean> findAll(String userId) {
        Collection<AlarmClock> allAlarmClock = findAll_(userId);
        Collection<AlarmClockBean> allBean = new ArrayList<AlarmClockBean>();

        for(AlarmClock alarmClock: allAlarmClock) {
            allBean.add(AlarmClockBean.EntityToBean(alarmClock));
        }

        return allBean;
    }

    private Collection<AlarmClock> findAll_(String userId) {
        return AlarmClockRepository.getInstance().findAllByUserId(userId);
    }

    public AlarmClockBean link(AlarmClockBean alarmClock, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne_(String.valueOf(alarmClock.getId()));
        if(a.getUser() != null)
            throw new ForbiddenMyLazyClockException();
        a.setUser(userId);
        a.setAddress(alarmClock.getAddress());
        a.setName(alarmClock.getName());
        a.setPreparationTime(alarmClock.getPreparationTime());
        a.setColor(alarmClock.getColor());
        return AlarmClockBean.EntityToBean(AlarmClockRepository.getInstance().save(a));
    }

    public AlarmClockBean unlink(String alarmClockId, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne_(alarmClockId);
        if(!a.getUser().equals(userId)) {
            throw new ForbiddenMyLazyClockException();
        }

        a.setUser("");
        a.setName("");
        a.setAddress("");
        a.setColor("");
        a.setPreparationTime(0);
        a.setCalendars(null);
        return AlarmClockBean.EntityToBean(AlarmClockRepository.getInstance().save(a));
    }

    public AlarmClockBean update(AlarmClockBean alarmClock, String userId) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne_(String.valueOf(alarmClock.getId()));
        if(!a.getUser().equals(userId))
            throw new ForbiddenMyLazyClockException();
        a.setAddress(alarmClock.getAddress());
        a.setName(alarmClock.getName());
        a.setPreparationTime(alarmClock.getPreparationTime());
        a.setColor(alarmClock.getColor());
        return AlarmClockBean.EntityToBean(
                AlarmClockRepository.getInstance().save(a)
        );
    }

    public AlarmClockBean generate() {
        AlarmClock alarmClock = AlarmClockRepository.getInstance().save(new AlarmClock());

        return AlarmClockBean.EntityToBean(alarmClock);
    }
}
