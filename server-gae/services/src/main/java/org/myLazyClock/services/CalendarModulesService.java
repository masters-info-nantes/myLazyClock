/*
 * myLazyClock
 *
 * Copyright (C) 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.myLazyClock.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.ArrayList;

import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.CalendarFactory;
import org.myLazyClock.calendarApi.CalendarStrategy;

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
	public CalendarEvent getFirstEventOfDay(int strategyId, Calendar date) throws EventNotFoundException{
		CalendarStrategy strategy = CalendarFactory.getInstance().get(strategyId);
		
		// Get ICS file and first event of the day
		CalendarEvent nextEvent = null;
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
