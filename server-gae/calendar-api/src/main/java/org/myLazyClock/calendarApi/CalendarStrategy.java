package org.myLazyClock.calendarApi;

import java.net.URL;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public interface CalendarStrategy {

    /**
     * The identifier is used to get a specific strategy in {@link CalendarStrategy}
     *
     * The identifier must be unique between all implementation of {@link CalendarStrategy}
     *
     * @return Identifier of the strategy
     */
    public Integer getId();

    /**
     * The name of the {@link CalendarStrategy} can be use by user
     * who want choice the strategy of travel
     *
     * @return Name of the {@link CalendarStrategy}
     */
    public String getName();
    
    /**
     * With specifique url get source on one day and return the first event of calandar of the day
     *
     * @param url url of source
     * @param day day in which search event
     * @return Date of FirstEvent
     * @throws EventNotFoundException if not event found in specific day
     */
    public Date getFirstEvent(String url, Calendar day) throws EventNotFoundException;
}
