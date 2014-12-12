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

package org.myLazyClock.restApi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import org.myLazyClock.services.CalendarService;
import org.myLazyClock.services.bean.CalendarBean;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;

import java.util.Collection;
import java.util.Objects;

/**
 * Created on 15/11/14.
 *
 * @author Jeremy
 */
@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class CalendarAPI {

    private MemcacheService getMemcacheService() {
        MemcacheService cache = MemcacheServiceFactory.getMemcacheService("calendar");
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Constants.MEMCACHE_LEVEL_ERROR_HANDLERS));
        return cache;
    }

    private void cleanCache(String key) {
        getMemcacheService().delete(key);
    }

    @ApiMethod(name = "calendar.list", httpMethod = ApiMethod.HttpMethod.GET, path="calendar")
    public Collection<CalendarBean> list(@Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }
        String keyCache = user.getUserId()+alarmClockId;
        Collection<CalendarBean> listCalendar = null;

        try {

            MemcacheService cache = getMemcacheService();
            try {
                listCalendar = (Collection<CalendarBean>) cache.get(keyCache);
            } catch (Exception ignore) { }

            if (listCalendar == null) {
                listCalendar = CalendarService.getInstance().findAll(alarmClockId, user);
                try {
                    cache.put(keyCache, listCalendar);
                } catch (Exception ignore) {}
            }

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
        return listCalendar;
    }

    @ApiMethod(name = "calendar.update", httpMethod = ApiMethod.HttpMethod.PUT, path="calendar")
    public CalendarBean update(@Named("calendarId") Long calendarId, @Named("alarmClockId") String alarmClockId, CalendarBean calendar, User user) throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        String keyCache = user.getUserId()+alarmClockId;

        try {

            CalendarBean calendarBean = CalendarService.getInstance().update(calendarId, alarmClockId, calendar, user);
            cleanCache(keyCache);

            return calendarBean;
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }

    }

    @ApiMethod(name = "calendar.add", httpMethod = ApiMethod.HttpMethod.POST, path="calendar")
    public CalendarBean add(@Named("alarmClockId") String alarmClockId, CalendarBean calendar, User user) throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {

            String keyCache = user.getUserId()+alarmClockId;

            CalendarBean calendarBean = CalendarService.getInstance().add(calendar, alarmClockId, user);
            cleanCache(keyCache);

            return calendarBean;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
    }

    @ApiMethod(name = "calendar.delete", httpMethod = ApiMethod.HttpMethod.DELETE, path="calendar")
    public void delete(@Named("calendarId") Long calendarId, @Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {

            String keyCache = user.getUserId()+alarmClockId;

            CalendarService.getInstance().delete(calendarId, alarmClockId, user);
            cleanCache(keyCache);

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
    }

    @ApiMethod(name = "calendar.item", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/item")
    public CalendarBean item(@Named("calendarId") Long calendarId, @Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {
            MemcacheService cache = getMemcacheService();

            String keyCache = user.getUserId()+alarmClockId;

            Collection<CalendarBean> listCalendar = null;

            try {
                listCalendar = (Collection<CalendarBean>) cache.get(keyCache);
            } catch (Exception ignore) { }

            if (listCalendar != null) {
                for (CalendarBean cal: listCalendar) {
                    if (Objects.equals(cal.getId(), calendarId)) {
                        return cal;
                    }
                }
            }

            return CalendarService.getInstance().findOne(calendarId, alarmClockId, user);

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
    }

}
