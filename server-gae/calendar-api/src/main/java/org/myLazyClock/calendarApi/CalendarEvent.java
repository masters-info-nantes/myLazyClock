package org.myLazyClock.calendarApi;

import java.util.Calendar;
import java.util.Date;

/**
 * Created on 16/11/14.
 *
 * @author dralagen
 */
public class CalendarEvent {

    private Date beginDate;
    private Date endDate;

    private String name;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
