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

package org.myLazyClock.model.repository;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.myLazyClock.model.model.MyLazyClockUser;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

/**
 * Created on 08/12/14.
 *
 * @author dralagen
 */
public class MyLazyClockUserRepository {

    private static MyLazyClockUserRepository repo = null;

    private MyLazyClockUserRepository() {
    }

    public static synchronized MyLazyClockUserRepository getInstance() {
        if (null == repo) {
            repo = new MyLazyClockUserRepository();
        }
        return repo;
    }

    public MyLazyClockUser findOne(String id) {
        return findOne(forgeKey(id));
    }

    public MyLazyClockUser findOne(Key userKey) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            return pm.detachCopy(pm.getObjectById(MyLazyClockUser.class, userKey));
        } catch (JDOObjectNotFoundException e) {
            return null;
        }
    }

    public MyLazyClockUser save(MyLazyClockUser user) {

        if (user.getKey() == null) {
            user.setKey(forgeKey(user.getUser().getUserId()));
        }

        MyLazyClockUser saved;
        PersistenceManager pm = PMF.get().getPersistenceManager();


        try {
            pm.makePersistent(user);
            saved = pm.detachCopy(user);
        } finally {
            pm.close();
        }

        return saved;
    }

    private Key forgeKey(String id) {
        return new KeyFactory.Builder(MyLazyClockUser.class.getSimpleName(), id).getKey();
    }
}
