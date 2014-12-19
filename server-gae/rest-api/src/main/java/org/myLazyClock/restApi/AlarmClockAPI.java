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
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import org.myLazyClock.services.AlarmClockService;
import org.myLazyClock.services.MyLazyClockMemcacheService;
import org.myLazyClock.services.bean.AlarmClockBean;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;
import org.myLazyClock.services.exception.MyLazyClockInvalidFormException;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;

import java.util.Collection;

/**
 * Created on 22/10/14.
 *
 * @author dralagen, Maxime
 */
@Api(
    name = Constants.NAME,
    version = Constants.VERSION,
    clientIds = { Constants.WEB_CLIENT_ID},
    scopes = {Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ}
)
public class AlarmClockAPI {

    @ApiMethod(name = "alarmClock.list", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock")
    public Collection<AlarmClockBean> getAllByUser(User user) throws UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        Collection<AlarmClockBean> listAlarmClock = MyLazyClockMemcacheService.getInstance().getListAlarmClock(user);
        if (listAlarmClock != null) {
            return listAlarmClock;
        }

        listAlarmClock = AlarmClockService.getInstance().findAll(user.getUserId());

        MyLazyClockMemcacheService.getInstance().addListAlarmClock(user, listAlarmClock);

        return listAlarmClock;
    }

    // Do not add an user because it's use by rasp
    @ApiMethod(name = "alarmClock.item", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/{alarmClockId}")
    public AlarmClockBean item(@Named("alarmClockId") Long alarmClockId)
            throws NotFoundException, UnauthorizedException, ForbiddenException {

        try {

            AlarmClockBean alarm = MyLazyClockMemcacheService.getInstance().getAlarmClock(alarmClockId);
            if (alarm != null) {
                return alarm;
            }

            alarm = AlarmClockService.getInstance().findOne(alarmClockId);

            MyLazyClockMemcacheService.getInstance().addAlarmClock(alarmClockId, alarm);

            return alarm;
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException(e);
        }
    }

    // Do not add an user because it's use by rasp
    @ApiMethod(name = "alarmClock.generate", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock")
    public AlarmClockBean generate() {
        return AlarmClockService.getInstance().generate();
    }

    @ApiMethod(name = "alarmClock.link", httpMethod = ApiMethod.HttpMethod.PUT, path="alarmClock")
    public AlarmClockBean link(AlarmClockBean alarmClock, User user)
            throws ForbiddenException, NotFoundException, UnauthorizedException, BadRequestException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {
            AlarmClockBean newAlarmClock = AlarmClockService.getInstance().link(alarmClock, user);

            // Clean cache
            MyLazyClockMemcacheService.getInstance().cleanAlarmClock(user, newAlarmClock.getId());

            return newAlarmClock;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException(e);
        } catch (MyLazyClockInvalidFormException e) {
            throw new BadRequestException(e);
        }
    }

    @ApiMethod(name = "alarmClock.unlink", httpMethod = ApiMethod.HttpMethod.DELETE, path="alarmClock/{alarmClockId}")
    public AlarmClockBean unlink(@Named("alarmClockId") Long alarmClockId, User user)
            throws ForbiddenException, NotFoundException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {
            AlarmClockBean newAlarmClock = AlarmClockService.getInstance().unlink(alarmClockId, user.getUserId());

            // Clean cache
            MyLazyClockMemcacheService.getInstance().cleanAllAlarmClock(user, newAlarmClock.getId());


            return newAlarmClock;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException(e);
        }
    }

    @ApiMethod(name = "alarmClock.update", httpMethod = ApiMethod.HttpMethod.PUT, path="alarmClock/{id}")
    public AlarmClockBean update(@Named("id") Long alarmClockId, AlarmClockBean alarmClock, User user)
            throws ForbiddenException, NotFoundException, UnauthorizedException, BadRequestException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {

            AlarmClockBean newAlarmClock = AlarmClockService.getInstance().update(alarmClock, user.getUserId());

            // Clean cache
            MyLazyClockMemcacheService.getInstance().cleanAlarmClock(user, newAlarmClock.getId());

            return newAlarmClock;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException(e);
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException(e);
        } catch (MyLazyClockInvalidFormException e) {
            throw new BadRequestException(e);
        }
    }

}
