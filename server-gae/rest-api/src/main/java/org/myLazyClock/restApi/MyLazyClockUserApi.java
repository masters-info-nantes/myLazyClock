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
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.MyLazyClockUser;
import org.myLazyClock.services.MyLazyClockUserService;

/**
 * Created on 08/12/14.
 *
 * @author dralagen
 */
@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class MyLazyClockUserApi {

    @ApiMethod(name = "myLazyClockUser.link", httpMethod = ApiMethod.HttpMethod.POST, path = "myLazyClockUser")
    public MyLazyClockUser linkUser(@Named("token") String token, User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        MyLazyClockUser myLazyClockUser = MyLazyClockUserService.getInstance().add(user, token);

        return myLazyClockUser;
    }
}
