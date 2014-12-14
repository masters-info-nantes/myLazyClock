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

import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.services.bean.AlarmClockBean;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;
import org.myLazyClock.services.exception.MyLazyClockInvalidFormException;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 22/10/14.
 *
 * @author dralagen, Maxime
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

    public AlarmClockBean findOne(Long alarmClockId)
            throws NotFoundMyLazyClockException {
        return AlarmClockBean.EntityToBean(findOne_(alarmClockId));
    }

    private AlarmClock findOne_(Long id) throws NotFoundMyLazyClockException {
        AlarmClock alarmClock = AlarmClockRepository.getInstance().findOne(id);

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

    public AlarmClockBean link(AlarmClockBean alarmClock, User user)
            throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException, MyLazyClockInvalidFormException {

        checkAlarmClock(alarmClock);

        AlarmClock a = findOne_(alarmClock.getId());
        if(a.getUser() != null) {
            throw new ForbiddenMyLazyClockException();
        }

        alarmClock.copyValueInEntity(a);
        a.setUser(user.getUserId());

        return AlarmClockBean.EntityToBean(AlarmClockRepository.getInstance().save(a));
    }

    public AlarmClockBean unlink(Long alarmClockId, String userId)
            throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock a = findOne_(alarmClockId);
        if(!userId.equals(a.getUser())) {
            throw new ForbiddenMyLazyClockException();
        }

        AlarmClockBean bean = new AlarmClockBean();
        bean.copyValueInEntity(a);
        a.setUser(null);
        a.setCalendars(null);

        return AlarmClockBean.EntityToBean(AlarmClockRepository.getInstance().save(a));
    }

    public AlarmClockBean update(AlarmClockBean alarmClock, String userId)
            throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException, MyLazyClockInvalidFormException {

        checkAlarmClock(alarmClock);

        AlarmClock a = findOne_(alarmClock.getId());
        if(!userId.equals(a.getUser()))
            throw new ForbiddenMyLazyClockException();

        alarmClock.copyValueInEntity(a);

        return AlarmClockBean.EntityToBean(
                AlarmClockRepository.getInstance().save(a)
        );
    }

    public AlarmClockBean generate() {
        AlarmClock alarmClock = AlarmClockRepository.getInstance().save(new AlarmClock());

        return AlarmClockBean.EntityToBean(alarmClock);
    }

    /**
     * Check if {@link org.myLazyClock.services.bean.AlarmClockBean} is valid
     *
     * @param alarm AlarmClockBean checked
     * @throws MyLazyClockInvalidFormException if one field have an error
     */
    private void checkAlarmClock(AlarmClockBean alarm)
            throws MyLazyClockInvalidFormException {

        Map<String, String> errors = new HashMap<>();

        if (checkStringIsNull(alarm.getName())) {
            errors.put("name", "Name can not be empty");
        }

        if (checkStringIsNull(alarm.getRingtone())) {
            errors.put("ringtone", "Ringtone can not be empty");
        }

        if (checkStringIsNull(alarm.getAddress())) {
            errors.put("address", "Address can not be empty");
        }

        if (alarm.getPreparationTime() <= 0) {
            errors.put("preparationTime", "Preparation time can not be negative or null");
        }

        if (!errors.isEmpty()) {
            throw new MyLazyClockInvalidFormException(errors);
        }

    }

    /**
     * Check if string is null or empty
     *
     * @param field Field checked
     * @return True if string equals null or equals ""
     */
    private boolean checkStringIsNull(String field) {
        return field == null || "".equals(field);
    }
}
