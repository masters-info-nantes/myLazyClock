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
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import org.myLazyClock.services.CalendarService;
import org.myLazyClock.services.MyLazyClockMemcacheService;
import org.myLazyClock.services.bean.CalendarBean;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;
import org.myLazyClock.services.exception.MyLazyClockInvalidFormException;

import java.util.Collection;

/**
 * Created on 15/11/14.
 *
 * @author dralagen, Jeremy
 */
@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class CalendarAPI {

    @ApiMethod(name = "calendar.list", httpMethod = ApiMethod.HttpMethod.GET, path="calendar")
    public Collection<CalendarBean> list(@Named("alarmClockId") Long alarmClockId, User user)
            throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        Collection<CalendarBean> listCalendar = MyLazyClockMemcacheService.getInstance().getListCalendar(user, alarmClockId);;

        try {
            if (listCalendar == null) {

                listCalendar = CalendarService.getInstance().findAll(alarmClockId, user);

                MyLazyClockMemcacheService.getInstance().addListCalendar(user, alarmClockId, listCalendar);
            }

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        }
        return listCalendar;
    }

    @ApiMethod(name = "calendar.update", httpMethod = ApiMethod.HttpMethod.PUT, path="calendar")
    public CalendarBean update(@Named("calendarId") Long calendarId, @Named("alarmClockId") Long alarmClockId, CalendarBean calendar, User user)
            throws ForbiddenException, UnauthorizedException, BadRequestException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {

            CalendarBean calendarBean = CalendarService.getInstance().update(calendarId, alarmClockId, calendar, user);

            MyLazyClockMemcacheService.getInstance().cleanCalendar(user, alarmClockId);

            return calendarBean;
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        } catch (MyLazyClockInvalidFormException e) {
            throw new BadRequestException(e);
        }

    }

    @ApiMethod(name = "calendar.add", httpMethod = ApiMethod.HttpMethod.POST, path="calendar")
    public CalendarBean add(@Named("alarmClockId") Long alarmClockId, CalendarBean calendar, User user)
            throws ForbiddenException, UnauthorizedException, BadRequestException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {

            CalendarBean calendarBean = CalendarService.getInstance().add(calendar, alarmClockId, user);

            MyLazyClockMemcacheService.getInstance().cleanCalendar(user, alarmClockId);

            return calendarBean;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        } catch (MyLazyClockInvalidFormException e) {
            throw new BadRequestException(e);
        }
    }

    @ApiMethod(name = "calendar.delete", httpMethod = ApiMethod.HttpMethod.DELETE, path="calendar")
    public void delete(@Named("calendarId") Long calendarId, @Named("alarmClockId") Long alarmClockId, User user)
            throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {

            CalendarService.getInstance().delete(calendarId, alarmClockId, user);

            MyLazyClockMemcacheService.getInstance().cleanCalendar(user, alarmClockId);

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        }
    }

    @ApiMethod(name = "calendar.item", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/item")
    public CalendarBean item(@Named("calendarId") Long calendarId, @Named("alarmClockId") Long alarmClockId, User user)
            throws ForbiddenException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        CalendarBean calendar = MyLazyClockMemcacheService.getInstance().getCalendar(user, alarmClockId, calendarId);

        if (calendar != null) {
            return calendar;
        }

        try {

            return CalendarService.getInstance().findOne(calendarId, alarmClockId, user);

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        }
    }

}
