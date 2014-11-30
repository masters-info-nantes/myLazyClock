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

package org.myLazyClock.model.bean;

import java.util.Date;

/**
 * Created on 16/11/14.
 *
 * @author Maxime
 */
public class AlarmClockEvent {

    private Date beginDate;
    private Long travelDuration;
    private String name;
    private String address;


    public Date getBeginDate() {
        return beginDate;
    }

    public Long getBeginDateTime() {
        return beginDate.getTime();
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Long getTravelDuration() {
        return travelDuration;
    }

    public String getTravelDurationString() {
        Long h = travelDuration/3600;
        Long m = (travelDuration-(h*3600))/60;
        Long s = travelDuration-(h*3600+m*60);
        if (h != 0)
            return h+" heure(s) et "+m+" minute(s)";
        else {
            return m+" minute(s)";
        }
    }

    public void setTravelDuration(Long travelDuration) {
        this.travelDuration = travelDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
