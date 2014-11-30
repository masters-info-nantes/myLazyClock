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
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.services.CalendarService;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;

import java.util.Collection;

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

    @ApiMethod(name = "calendar.list", httpMethod = ApiMethod.HttpMethod.GET, path="calendar")
    public Collection<Calendar> list(@Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException {
        try {
            return CalendarService.getInstance().findAll(alarmClockId, user);
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
    }

    //TODO dralagen 29/11/14 : Implement update
    @ApiMethod(name = "calendar.update", httpMethod = ApiMethod.HttpMethod.PUT, path="calendar")
    public Calendar update(@Named("calendarId") Long calendarId, @Named("alarmClockId") Long alarmClockId, Calendar calendar, User user) throws ForbiddenException {
        try {
            if (user == null) {
                throw new ForbiddenMyLazyClockException();
            }
            return CalendarService.getInstance().update(calendarId, alarmClockId, calendar, user);
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
    }

    @ApiMethod(name = "calendar.add", httpMethod = ApiMethod.HttpMethod.POST, path="calendar")
    public Calendar add(@Named("alarmClockId") String alarmClockId, Calendar calendar, User user) throws ForbiddenException {
        try {
            return CalendarService.getInstance().add(calendar, alarmClockId, user);
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
    }

    //TODO dralagen 29/11/14 : Implement delete
    @ApiMethod(name = "calendar.delete", httpMethod = ApiMethod.HttpMethod.DELETE, path="calendar")
    public void delete(@Named("calendarId") Long calendarId, @Named("alarmClockId") Long alarmClockId, User user) {

    }

    @ApiMethod(name = "calendar.item", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/item")
    public Calendar item(@Named("calendarId") Long calendarId, @Named("alarmClockId") Long alarmClockId, User user) throws ForbiddenException {

        try {
            return CalendarService.getInstance().findOne(calendarId, alarmClockId, user);
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        }
    }

}
