package org.myLazyClock.model.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.impl.translate.EnumTranslatorFactory;

/**
 * Created by Maxime on 22/10/14.
 */
@Entity
public class AlarmClock {

    @Id
    private Long id;

    @Index
    private String user;

    private String name;

    private String address;

    private String defaultEventLocation;

    private int preparationTime;

    public AlarmClock() {}

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

    public String getDefaultEventLocation() {
        return defaultEventLocation;
    }

    public void setDefaultEventLocation(String defaultEventLocation) {
        this.defaultEventLocation = defaultEventLocation;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

}
