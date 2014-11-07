package org.myLazyClock.calendarApi;

import java.net.URL;
import java.io.IOException;

import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.data.ParserException;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarManualStrategy implements CalendarStrategy {

    public static final int ID = 1;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar Manual";
    }
    
    @Override
    public String getEdt(URL edtUrl) throws IOException{
    	return "No ics";
    }
    
    @Override
    public VEvent getFirstEvent(String icsFile, java.util.Calendar day) throws IOException, ParserException{
    	return null;
    }
}
