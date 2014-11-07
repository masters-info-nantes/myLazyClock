package org.myLazyClock.services;

import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import org.myLazyClock.calendarApi.CalendarFactory;
import org.myLazyClock.calendarApi.CalendarStrategy;

import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.data.ParserException;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarModulesService {

	/**
	 * Return the first event in specified day on online schedule
	 * 
	 * @param strategyId Calendar strategy used
	 * @param date Day where to search event
	 * @return First event of day if exist, null otherwise
	 */
	public VEvent getFirstEventOfDay(int strategyId, java.util.Calendar date){
		CalendarStrategy strategy = CalendarFactory.getInstance().get(strategyId);
		
		// Get ICS file and first event of the day
		VEvent nextEvent = null;
		try{
			URL url = new URL("https://edt.univ-nantes.fr/staps/g93108.ics");	
			String icsFile = strategy.getEdt(url);	
			nextEvent = strategy.getFirstEvent(icsFile, date);
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		catch(ParserException ex){
			ex.printStackTrace();
		}
		
		return nextEvent;
	}
	
    /**
     * List all module who implement {@link CalendarStrategy}
     *
     * @return a String at this format : "Name : Id \n"
     */
    public String listModule() {
        String result = "";

        CalendarFactory factory = CalendarFactory.getInstance();

        for ( CalendarStrategy strategy : factory) {
            result += strategy.getName() + " : " + strategy.getId() + "\n";
        }

        return result;
    }
}
