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

package org.myLazyClock.model.model;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by Maxime on 22/10/14.
 */
@PersistenceCapable
public class AlarmClock {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String user;

    @Persistent
    private String name;

    @Persistent
    private String address;

    @Persistent
    private String color;

    @Persistent
    private int preparationTime;

    //@Persistent
    //private List<Calendar> calendars;

    public AlarmClock() {}

    public AlarmClock(Long id, String user, String address, String color, int preparationTime) {
        this.id = id;
        this.user = user;
        this.address = address;
        this.color = color;
        this.preparationTime = preparationTime;
    }

    public Long getId () {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getPreparationTimeString() {
        int h = preparationTime/3600;
        int m = (preparationTime-(h*3600))/60;
        int s = preparationTime-(h*3600+m*60);
        if (h != 0)
            return h+" heure(s) et "+m+" minute(s)";
        else {
            return m+" minute(s)";
        }
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /*
    public List<Calendar> getCalendars () {
        if (calendars == null) {
            return new ArrayList<Calendar>();
        }
        return calendars;
    }

    public void setCalendars (List<Calendar> calendars) {
        this.calendars = calendars;
    }
    */

}
