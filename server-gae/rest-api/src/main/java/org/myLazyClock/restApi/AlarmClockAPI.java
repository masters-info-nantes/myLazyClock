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
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import org.myLazyClock.services.AlarmClockService;
import org.myLazyClock.services.bean.AlarmClockBean;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;
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

    private MemcacheService getMemcacheService() {
        MemcacheService cache = MemcacheServiceFactory.getMemcacheService("alarmClock");
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Constants.MEMCACHE_LEVEL_ERROR_HANDLERS));
        return cache;
    }

    private void cleanCache(Object key) {
        getMemcacheService().delete(key);
    }

    @ApiMethod(name = "alarmClock.list", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/list")
    public Collection<AlarmClockBean> getAllByUser(User user) throws UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        Collection<AlarmClockBean> listAlarmClock;
        MemcacheService cache = getMemcacheService();
        try {
            listAlarmClock = (Collection<AlarmClockBean>) cache.get(user);
            if (listAlarmClock != null) {
                return listAlarmClock;
            }
        } catch (Exception ignore) {}

        listAlarmClock = AlarmClockService.getInstance().findAll(user.getUserId());

        try {
            cache.put(user, listAlarmClock);
        } catch (Exception ignore) {}

        return listAlarmClock;
    }

    // Do not add an user because it's use by rasp
    @ApiMethod(name = "alarmClock.item", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/item")
    public AlarmClockBean item(@Named("alarmClockId") Long alarmClockId) throws NotFoundException, UnauthorizedException, ForbiddenException {

        try {
            AlarmClockBean alarm;
            MemcacheService cache = getMemcacheService();
            try {
                alarm = (AlarmClockBean) cache.get(alarmClockId);
                if (alarm != null) {
                    return alarm;
                }
            } catch (Exception ignore) {}
            alarm = AlarmClockService.getInstance().findOne(alarmClockId);

            try {
                cache.put(alarmClockId, alarm);
            } catch (Exception ignore) { }

            return alarm;
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

    // Do not add an user because it's use by rasp
    @ApiMethod(name = "alarmClock.generate", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/generate")
    public AlarmClockBean generate() {
        return AlarmClockService.getInstance().generate();
    }

    @ApiMethod(name = "alarmClock.link", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock/link")
    public AlarmClockBean link(AlarmClockBean alarmClock, User user) throws ForbiddenException, NotFoundException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {
            AlarmClockBean newAlarmClock = AlarmClockService.getInstance().link(alarmClock, user);
            cleanCache(user);
            cleanCache(newAlarmClock.getId());

            return newAlarmClock;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

    @ApiMethod(name = "alarmClock.unlink", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock/unlink")
    public AlarmClockBean unlink(@Named("alarmClockId") Long alarmClockId, User user) throws ForbiddenException, NotFoundException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {
            AlarmClockBean newAlarmClock = AlarmClockService.getInstance().unlink(alarmClockId, user.getUserId());
            cleanCache(user);
            cleanCache(newAlarmClock.getId());

            return newAlarmClock;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

    @ApiMethod(name = "alarmClock.update", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock/update")
    public AlarmClockBean update(AlarmClockBean alarmClock, User user) throws ForbiddenException, NotFoundException, UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        try {

            AlarmClockBean newAlarmClock = AlarmClockService.getInstance().update(alarmClock, user.getUserId());
            cleanCache(user);
            cleanCache(newAlarmClock.getId());

            return newAlarmClock;

        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

}
