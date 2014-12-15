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
import org.myLazyClock.services.exception.MyLazyClockInvalidFormException;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public Collection<CalendarBean> findAll(Long alarmClockId, User user) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        Collection<Calendar> allCalendar =  findAll_(alarmClockId, user);

        Collection<CalendarBean> allBean = new ArrayList<>();

        for(Calendar oneCal: allCalendar) {
            allBean.add(CalendarBean.EntityToBean(oneCal));
        }

        return allBean;
    }

    private Collection<Calendar> findAll_(Long alarmClockId, User user) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {
        AlarmClock alarm = findOneAlarmClock_(alarmClockId);

        if (!user.getUserId().equals(alarm.getUser())) {
            throw new ForbiddenMyLazyClockException();
        }

        return CalendarRepository.getInstance().findAll(alarm);
    }

    public CalendarBean add (CalendarBean calendar, Long alarmClockId, User user) throws ForbiddenMyLazyClockException, MyLazyClockInvalidFormException, NotFoundMyLazyClockException {

        checkCalendar(calendar);

        AlarmClock alarm = findOneAlarmClock_(alarmClockId);

        if (!user.getUserId().equals(alarm.getUser())) {
            throw  new ForbiddenMyLazyClockException();
        }

        return CalendarBean.EntityToBean(
                CalendarRepository.getInstance().add(calendar.toEntity(), alarm)
        );
    }

    public CalendarBean findOne(Long calendarId, Long alarmClockId, User user) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {

        AlarmClock alarmClock = findOneAlarmClock_(alarmClockId);

        if (!user.getUserId().equals(alarmClock.getUser())) {
            throw new ForbiddenMyLazyClockException();
        }

        return CalendarBean.EntityToBean(findOne_(calendarId, alarmClockId));
    }

    private Calendar findOne_(Long calendarId, Long alarmClockId) throws ForbiddenMyLazyClockException {
        Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), alarmClockId)
                .addChild(Calendar.class.getSimpleName(), calendarId)
                .getKey();

        return CalendarRepository.getInstance().findOne(calendarKey);
    }

    private AlarmClock findOneAlarmClock_(Long alarmClockId) throws NotFoundMyLazyClockException {
        AlarmClock alarmClock = AlarmClockRepository.getInstance().findOne(alarmClockId);

        if (alarmClock == null) {
            throw new NotFoundMyLazyClockException("Not found alarm clock " + alarmClockId);
        }

        return alarmClock;
    }

    public CalendarBean update(Long calendarId, Long alarmClockId, CalendarBean calendar, User user) throws ForbiddenMyLazyClockException, MyLazyClockInvalidFormException, NotFoundMyLazyClockException {

        checkCalendar(calendar);

        Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), alarmClockId)
                                    .addChild(Calendar.class.getSimpleName(), calendarId)
                                    .getKey();

        AlarmClock alarmClock = findOneAlarmClock_(alarmClockId);

        if (!user.getUserId().equals(alarmClock.getUser())) {
            throw new ForbiddenMyLazyClockException();
        }

        Calendar toSave = CalendarRepository.getInstance().findOne(calendarKey);

        toSave.setName(calendar.getName());
        toSave.setParam(calendar.getParam());
        toSave.setCalendarType(calendar.getCalendarType());
        toSave.setTravelMode(calendar.getTravelMode());
        toSave.setDefaultEventLocation(calendar.getDefaultEventLocation());
        toSave.setUseAlwaysDefaultLocation(calendar.isUseAlwaysDefaultLocation());

        return CalendarBean.EntityToBean(CalendarRepository.getInstance().save(toSave));
    }

    public void delete(Long calendarId, Long alarmClockId, User user) throws ForbiddenMyLazyClockException, NotFoundMyLazyClockException {

        AlarmClock alarmClock = findOneAlarmClock_(alarmClockId);

        if (!alarmClock.getUser().equals(user.getUserId())) {
            throw new ForbiddenMyLazyClockException();
        }

        Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), alarmClockId)
                                .addChild(Calendar.class.getSimpleName(), calendarId)
                                .getKey();
        Calendar calendar = CalendarRepository.getInstance().findOne(calendarKey);

        CalendarRepository.getInstance().delete(calendar);
    }

    /**
     * Check if {@link org.myLazyClock.services.bean.CalendarBean} is valid
     *
     * @param calendar CalendarBean checked
     * @throws MyLazyClockInvalidFormException if one field have an error
     */
    private void checkCalendar(CalendarBean calendar) throws MyLazyClockInvalidFormException {
        Map<String, String> errors = new HashMap<>();

        if (calendar.getAlarmClockId() == null) {
            errors.put("alarmClockId", "alarmClockId can not be null");
        }

        if (checkStringIsNull(calendar.getName())) {
            errors.put("name", "name can not be empty");
        }

        if (checkStringIsNull(calendar.getParam())) {
            errors.put("param", "param can not be empty");
        }

        if (checkStringIsNull(calendar.getCalendarType())) {
            errors.put("calendarType", "calendarType can not be empty");
        }

        if (calendar.getTravelMode() == null) {
            errors.put("travelMode", "travelMode can not be null");
        }

        if (checkStringIsNull(calendar.getDefaultEventLocation())) {
            errors.put("defaultEventLocation", "defaultEventLocation can not be empty");
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
