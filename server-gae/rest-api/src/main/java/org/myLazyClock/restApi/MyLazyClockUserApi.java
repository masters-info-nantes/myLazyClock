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

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.services.calendar.CalendarScopes;
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.MyLazyClockUser;
import org.myLazyClock.services.MyLazyClockUserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

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
    public MyLazyClockUser linkUser(@Named("code") String code, User user) throws UnauthorizedException, GeneralSecurityException, IOException {
        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                jsonFactory,
                "1072024627812-kgv1uou2btdphtvb2l2bbh14n6u2n2mg.apps.googleusercontent.com",
                "K645ivQD06Ll2ELmhN9zlGU_",
                Arrays.asList(CalendarScopes.CALENDAR_READONLY)
        ).build();

        GoogleTokenResponse response=flow.newTokenRequest(code).setRedirectUri("http://localhost").execute();

        return MyLazyClockUserService.getInstance().add(user, response.getRefreshToken());
    }

    @ApiMethod(name = "myLazyClockUser.get", httpMethod = ApiMethod.HttpMethod.GET, path = "myLazyClockUser")
    public MyLazyClockUser findOne(User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Login Required");
        }

        return MyLazyClockUserService.getInstance().findOne(user.getUserId());
    }
}
