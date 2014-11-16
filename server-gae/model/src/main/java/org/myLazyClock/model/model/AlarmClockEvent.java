package org.myLazyClock.model.model;

import java.util.Date;

/**
 * Created by Maxime on 16/11/14.
 */
public class AlarmClockEvent {

    private Date beginDate;
    private Long travelDuration;


    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Long getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(Long travelDuration) {
        this.travelDuration = travelDuration;
    }
}
