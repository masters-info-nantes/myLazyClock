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
public class MyLazyClockUserValide {

    private User user;

    private boolean isValide;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isValide() {
        return isValide;
    }

    public void setValide(boolean isValide) {
        this.isValide = isValide;
    }

    public static MyLazyClockUserValide fromMyLazyClockUser(MyLazyClockUser lazyClockUser) {
        MyLazyClockUserValide bean = new MyLazyClockUserValide();

        if (lazyClockUser == null) {
            bean.setValide(false);
            return bean;
        }

        bean.setUser(lazyClockUser.getUser());

        bean.setValide(
                !( lazyClockUser.getToken() == null
                    || lazyClockUser.getToken().equals("")
                )
        );

        return bean;
    }
}
