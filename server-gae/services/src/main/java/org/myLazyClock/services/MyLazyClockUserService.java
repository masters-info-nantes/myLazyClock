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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.MyLazyClockUser;
import org.myLazyClock.model.repository.MyLazyClockUserRepository;
import org.myLazyClock.services.bean.MyLazyClockUserValid;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created on 08/12/14.
 *
 * @author dralagen
 */
public class MyLazyClockUserService {

    private static MyLazyClockUserService service = null;

    private MyLazyClockUserService() {}

    public static synchronized MyLazyClockUserService getInstance() {
        if (null == service) {
            service = new MyLazyClockUserService();
        }
        return service;
    }

    public MyLazyClockUserValid add(User user, String token) {

        MyLazyClockUser lazyClockUser = new MyLazyClockUser();

        lazyClockUser.setToken(token);
        lazyClockUser.setUser(user);

        return MyLazyClockUserValid.fromMyLazyClockUser(
                MyLazyClockUserRepository.getInstance().save(lazyClockUser)
        );
    }

    public MyLazyClockUserValid checkValid(String userId) {
        MyLazyClockUser user = MyLazyClockUserRepository.getInstance().findOne(userId);

        if (user == null) {
            return new MyLazyClockUserValid();
        }

        HttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(ConstantAPI.API_ID, ConstantAPI.API_SECRET)
                .build().setRefreshToken(user.getToken());

        try {
            credential.refreshToken();
        } catch (IOException ignore) {}


        if (credential.getAccessToken() == null || credential.getAccessToken().equals("")) {
            user.setToken(null);
            MyLazyClockUserRepository.getInstance().save(user);
        }

        return MyLazyClockUserValid.fromMyLazyClockUser(user);
    }
}
