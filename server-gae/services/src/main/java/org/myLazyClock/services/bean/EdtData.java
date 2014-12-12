package org.myLazyClock.services.bean;

import java.io.Serializable;

/**
 * Created on 27/11/14.
 *
 * @author Maxime
 */
public class EdtData implements Serializable {

    private String name;
    private String id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
