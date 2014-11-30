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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.model.repository.CalendarRepository;
import org.myLazyClock.model.repository.PMF;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Collection;
import java.util.List;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarService {

    private static CalendarService service = null;

    private CalendarService () {}

    public static synchronized CalendarService getInstance() {
        if (null == service) {
            service = new CalendarService();
        }
        return service;
    }


    public Collection<Calendar> findAll (String alarmClockId, User user) throws ForbiddenMyLazyClockException {
        AlarmClock alarm = AlarmClockRepository.getInstance().findOne(alarmClockId);

        if (!alarm.getUser().equals(user.getUserId())) {
            throw new ForbiddenMyLazyClockException();
        }

        return CalendarRepository.getInstance().findAll(alarm);
    }

    public Calendar add (Calendar calendar, String alarmClockId, User user) throws ForbiddenMyLazyClockException {
        AlarmClock alarm = AlarmClockRepository.getInstance().findOne(alarmClockId);

        if (!alarm.getUser().equals(user.getUserId())) {
            throw  new ForbiddenMyLazyClockException();
        }

        return CalendarRepository.getInstance().add(calendar, alarm);
    }

    public Calendar findOne(Long calendarId, Long alarmClockId, User user) {
        //Key calendarKey = KeyFactory.createKey(Calendar.class.getSimpleName(), calendarId);
        Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), alarmClockId)
                .addChild(Calendar.class.getSimpleName(), calendarId)
                .getKey();

        Calendar calendar = CalendarRepository.getInstance().findOne(calendarKey);
        calendar.setId(calendar.getKey().getId());
        return calendar;
    }
}
