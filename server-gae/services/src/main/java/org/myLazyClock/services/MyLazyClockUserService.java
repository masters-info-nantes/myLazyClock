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
import org.myLazyClock.model.model.MyLazyClockUser;
import org.myLazyClock.model.repository.MyLazyClockUserRepository;

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

    public MyLazyClockUser findOne(String id) {
        Key userKey = forgeKey(id);
        return MyLazyClockUserRepository.getInstance().findOne(userKey);
    }

    public MyLazyClockUser add(User user, String token) {
        Key userKey = forgeKey(user.getUserId());

        MyLazyClockUser lazyClockUser = new MyLazyClockUser();

        lazyClockUser.setKey(userKey);
        lazyClockUser.setToken(token);
        lazyClockUser.setUser(user);

        return MyLazyClockUserRepository.getInstance().save(lazyClockUser);
    }

    private Key forgeKey(String id) {
        return new KeyFactory.Builder(MyLazyClockUser.class.getSimpleName(), id).getKey();
    }
}
