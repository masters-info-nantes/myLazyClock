package org.myLazyClock.model.model;

import com.googlecode.objectify.annotation.*;

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
}
