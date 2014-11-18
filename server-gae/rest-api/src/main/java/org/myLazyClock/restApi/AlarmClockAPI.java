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
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.AlarmClockEvent;
import org.myLazyClock.services.AlarmClockService;
import org.myLazyClock.services.exception.ForbiddenMyLazyClockException;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;

import java.util.Collection;

/**
 * Created by Maxime on 22/10/14.
 */

@Api(
    name = Constants.NAME,
    version = Constants.VERSION,
    clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
    scopes = {Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ}
)
public class AlarmClockAPI {

    @ApiMethod(name = "alarmClock.list", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/list")
    public Collection<AlarmClock> getAllByUser(User user) {
        return AlarmClockService.getInstance().findAll(user.getUserId());
    }

    @ApiMethod(name = "alarmClock.item", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/item")
    public AlarmClock item(@Named("alarmClockId") String alarmClockId) throws NotFoundException{
        try {
            return AlarmClockService.getInstance().item(alarmClockId);
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

    @ApiMethod(name = "alarmClock.generate", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/generate")
    public AlarmClock generate() {
        return AlarmClockService.getInstance().generate();
    }

    @ApiMethod(name = "alarmClock.link", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock/link")
    public AlarmClock link(AlarmClock alarmClock, User user) throws ForbiddenException, NotFoundException{
        try {
            return AlarmClockService.getInstance().link(alarmClock, user.getUserId());
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

    @ApiMethod(name = "alarmClock.unlink", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock/unlink")
    public AlarmClock unlink(@Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException, NotFoundException{
        try {
            return AlarmClockService.getInstance().unlink(alarmClockId, user.getUserId());
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

    @ApiMethod(name = "alarmClock.update", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock/update")
    public AlarmClock update(AlarmClock alarmClock, User user) throws ForbiddenException, NotFoundException{
        try {
            return AlarmClockService.getInstance().update(alarmClock, user.getUserId());
        } catch (ForbiddenMyLazyClockException e) {
            throw new ForbiddenException("Forbidden");
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException("NotFound");
        }
    }

}
