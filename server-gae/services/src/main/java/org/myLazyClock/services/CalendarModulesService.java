package org.myLazyClock.services;

import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;

import org.myLazyClock.calendarApi.CalendarFactory;
import org.myLazyClock.calendarApi.CalendarStrategy;

import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.data.ParserException;
import org.myLazyClock.calendarApi.EventNotFoundException;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarModulesService {

    private static CalendarModulesService service = null;

    private CalendarModulesService() {}

    public static synchronized CalendarModulesService getInstance() {
        if (null == service) {
            service = new CalendarModulesService();
        }
        return service;
    }

	/**
	 * Return the first event in specified day on online schedule
	 * 
	 * @param strategyId Calendar strategy used
	 * @param date Day where to search event
	 * @return First event of day if exist, null otherwise
	 */
	public Date getFirstEventOfDay(int strategyId, Calendar date) throws EventNotFoundException{
		CalendarStrategy strategy = CalendarFactory.getInstance().get(strategyId);
		
		// Get ICS file and first event of the day
		Date nextEvent = null;
		nextEvent = strategy.getFirstEvent("https://edt.univ-nantes.fr/staps/g93108.ics", date);
			
		return nextEvent;
	}
	
    /**
     * List all module who implement {@link CalendarStrategy}
     *
     * @return a Collection of all Calendar modules
     */
    public Collection<CalendarStrategy> listModule() {
        Collection<CalendarStrategy> result = new ArrayList<CalendarStrategy>();

        CalendarFactory factory = CalendarFactory.getInstance();

        for ( CalendarStrategy strategy : factory) {
            result.add(strategy);
        }

        return result;
    }

}
