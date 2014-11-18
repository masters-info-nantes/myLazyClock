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

/**
 * Created by Maxime on 16/11/14.
 */
public class Calendar {

    private Long id; //id datastore

    private String name;

    private String param; // id du gcal ou url ics ...

    private String calendarType;

    private String travelMode;

    private String defaultEventLocation;

    private boolean useAlwaysDefaultLocation;


    public Long getId() {
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

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
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
}
