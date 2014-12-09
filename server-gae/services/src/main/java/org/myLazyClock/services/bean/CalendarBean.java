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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.model.model.TravelMode;

/**
 * Created on 04/12/14.
 *
 * @author dralagen
 */
public class CalendarBean {

    private Long id;

    private Long alarmClockId;

    private String name;

    private String param;

    private String calendarType;

    private TravelMode travelMode;

    private String defaultEventLocation;

    private boolean useAlwaysDefaultLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlarmClockId() {
        return alarmClockId;
    }

    public void setAlarmClockId(Long alarmClockId) {
        this.alarmClockId = alarmClockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }

    public TravelMode getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public String getDefaultEventLocation() {
        return defaultEventLocation;
    }

    public void setDefaultEventLocation(String defaultEventLocation) {
        this.defaultEventLocation = defaultEventLocation;
    }

    public boolean isUseAlwaysDefaultLocation() {
        return useAlwaysDefaultLocation;
    }

    public void setUseAlwaysDefaultLocation(boolean useAlwaysDefaultLocation) {
        this.useAlwaysDefaultLocation = useAlwaysDefaultLocation;
   }

    public Calendar toEntity() {

        Calendar newEntity = new Calendar();

        if (id != null) {
            Key calendarKey = new KeyFactory.Builder(AlarmClock.class.getSimpleName(), getAlarmClockId())
                    .addChild(Calendar.class.getSimpleName(), getId())
                    .getKey();

            newEntity.setKey(calendarKey);
        }

        newEntity.setName(getName());
        newEntity.setParam(getParam());
        newEntity.setCalendarType(getCalendarType());
        newEntity.setTravelMode(getTravelMode());
        newEntity.setDefaultEventLocation(getDefaultEventLocation());
        newEntity.setUseAlwaysDefaultLocation(isUseAlwaysDefaultLocation());

        return newEntity;

    }

    public void fromEntity(Calendar calendar) {
        setId(calendar.getKey().getId());
        setAlarmClockId(calendar.getKey().getParent().getId());

        setName(calendar.getName());
        setParam(calendar.getParam());
        setCalendarType(calendar.getCalendarType());
        setTravelMode(calendar.getTravelMode());
        setDefaultEventLocation(calendar.getDefaultEventLocation());
        setUseAlwaysDefaultLocation(calendar.isUseAlwaysDefaultLocation());
    }

    public static CalendarBean EntityToBean(Calendar calendar) {
        CalendarBean newBean = new CalendarBean();

        newBean.fromEntity(calendar);

        return newBean;
    }
}
