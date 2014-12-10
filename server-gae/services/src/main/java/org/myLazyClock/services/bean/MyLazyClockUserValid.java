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

package org.myLazyClock.services.bean;

import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.MyLazyClockUser;

/**
 * Created on 08/12/14.
 *
 * @author dralagen
 */
public class MyLazyClockUserValid {

    private User user;

    private boolean isValid = false;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public static MyLazyClockUserValid fromMyLazyClockUser(MyLazyClockUser lazyClockUser) {
        MyLazyClockUserValid bean = new MyLazyClockUserValid();

        if (lazyClockUser == null) {
            bean.setValid(false);
            return bean;
        }

        bean.setUser(lazyClockUser.getUser());

        bean.setValid(
                !(lazyClockUser.getToken() == null
                        || lazyClockUser.getToken().equals("")
                )
        );

        return bean;
    }
}
