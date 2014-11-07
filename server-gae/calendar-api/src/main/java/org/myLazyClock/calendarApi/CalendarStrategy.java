package org.myLazyClock.calendarApi;

import java.net.URL;
import java.io.IOException;
import net.fortuna.ical4j.data.ParserException;

import net.fortuna.ical4j.model.component.VEvent;

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
     * Get ICS file on the given server
     * @param edtUrl Path to get ics file in schedule
     * @return ICS file as string
     * @throws IOException
     */
    public String getEdt(URL edtUrl) throws IOException;
    
    /**
     * Construct calendar from given ICS file
     * and return first event of given day
     * @param icsFile ics file as string
     * @param day day in which search event
     * @return First event of the day or null if no events
     * @throws IOException
     * @throws ParserException
     */
    public VEvent getFirstEvent(String icsFile, java.util.Calendar day) throws IOException, ParserException;   
}
