package org.myLazyClock.model.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Collection;

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
