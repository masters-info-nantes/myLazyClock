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

import org.myLazyClock.model.model.AlarmClock;

import java.io.Serializable;

/**
 * Created on 04/12/14.
 *
 * @author dralagen
 */
public class AlarmClockBean implements Serializable {

    private Long id;

    private String user;

    private String name;

    private String ringtone;

    private String address;

    private String color;

    private int preparationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRingtone () {
        return ringtone;
    }

    public void setRingtone (String ringtone) {
        this.ringtone = ringtone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public AlarmClock toEntity() {
        AlarmClock alarmClock = new AlarmClock();

        alarmClock.setId(getId());
        alarmClock.setUser(getUser());

        copyValueInEntity(alarmClock);

        return alarmClock;
    }

    public void copyValueInEntity(AlarmClock alarmClock) {

        alarmClock.setName(getName());
        alarmClock.setRingtone(getRingtone());
        alarmClock.setAddress(getAddress());
        alarmClock.setColor(getColor());
        alarmClock.setPreparationTime(getPreparationTime());

    }

    public void fromEntity(AlarmClock alarmClock) {
        setId(alarmClock.getId());
        setUser(alarmClock.getUser());
        setName(alarmClock.getName());
        setRingtone(alarmClock.getRingtone());
        setAddress(alarmClock.getAddress());
        setColor(alarmClock.getColor());
        setPreparationTime(alarmClock.getPreparationTime());
    }

    public static AlarmClockBean EntityToBean(AlarmClock alarmClock) {
        AlarmClockBean newBean = new AlarmClockBean();

        newBean.fromEntity(alarmClock);

        return newBean;
    }
}
