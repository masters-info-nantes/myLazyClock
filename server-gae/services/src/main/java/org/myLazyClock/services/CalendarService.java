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
import org.myLazyClock.services.bean.CalendarBean;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;

import java.util.ArrayList;
import java.util.Collection;

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

    public Collection<CalendarBean> findAll(String alarmClockId, User user) throws ForbiddenMyLazyClockException {
        Collection<Calendar> allCalendar =  findAll_(alarmClockId, user);

        Collection<CalendarBean> allBean = new ArrayList<CalendarBean>();

        for(Calendar oneCal: allCalendar) {
            allBean.add(CalendarBean.EntityToBean(oneCal));
        }

        return allBean;
    }

    private Collection<Calendar> findAll_(String alarmClockId, User user) throws ForbiddenMyLazyClockException {
        AlarmClock alarm = AlarmClockRepository.getInstance().findOne(Long.decode(alarmClockId));

        if (!alarm.getUser().equals(user.getUserId())) {
            throw new ForbiddenMyLazyClockException();
        }

        return CalendarRepository.getInstance().findAll(alarm);
    }

    public CalendarBean add (CalendarBean calendar, String alarmClockId, User user) throws ForbiddenMyLazyClockException {
        AlarmClock alarm = AlarmClockRepository.getInstance().findOne(Long.decode(alarmClockId));

        if (!alarm.getUser().equals(user.getUserId())) {
            throw  new ForbiddenMyLazyClockException();
        }

        return CalendarBean.EntityToBean(
                CalendarRepository.getInstance().add(calendar.toEntity(), alarm)
        );
    }

    public CalendarBean findOne(Long calendarId, Long alarmClockId, User user) throws ForbiddenMyLazyClockException {
        return CalendarBean.EntityToBean(findOne_(calendarId, alarmClockId, user));
    }

    private Calendar findOne_(Long calendarId, Long alarmClockId, User user) throws ForbiddenMyLazyClockException {

        Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), alarmClockId)
                .addChild(Calendar.class.getSimpleName(), calendarId)
                .getKey();

        return CalendarRepository.getInstance().findOne(calendarKey);
    }

    public CalendarBean update(Long calendarId, Long alarmClockId, CalendarBean calendar, User user) {
        Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), alarmClockId)
                                    .addChild(Calendar.class.getSimpleName(), calendarId)
                                    .getKey();
        Calendar toSave = CalendarRepository.getInstance().findOne(calendarKey);

        toSave.setName(calendar.getName());
        toSave.setParam(calendar.getParam());
        toSave.setCalendarType(calendar.getCalendarType());
        toSave.setTravelMode(calendar.getTravelMode());
        toSave.setDefaultEventLocation(calendar.getDefaultEventLocation());
        toSave.setUseAlwaysDefaultLocation(calendar.isUseAlwaysDefaultLocation());

        return CalendarBean.EntityToBean(CalendarRepository.getInstance().save(toSave));
    }

    public void delete(Long calendarId, Long alarmClockId, User user) throws ForbiddenMyLazyClockException {
        AlarmClock alarmClock = AlarmClockRepository.getInstance().findOne(alarmClockId);

        if (!alarmClock.getUser().equals(user.getUserId())) {
            throw new ForbiddenMyLazyClockException();
        }

        Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), alarmClockId)
                                .addChild(Calendar.class.getSimpleName(), calendarId)
                                .getKey();
        Calendar calendar = CalendarRepository.getInstance().findOne(calendarKey);

        CalendarRepository.getInstance().delete(calendar);
    }
}
