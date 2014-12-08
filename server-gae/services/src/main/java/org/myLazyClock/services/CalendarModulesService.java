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

import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.CalendarFactory;
import org.myLazyClock.calendarApi.CalendarStrategy;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.model.model.Calendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	 * @param calendar Calendar to use with settings (strategy, url)
	 * @param date Day where to search event
	 * @return First event of day if exist, null otherwise
	 */

	public CalendarEvent getFirstEventOfDay(Calendar calendar, java.util.Calendar date, String token) throws EventNotFoundException{


        int strategyId = 1; // ICS_FILE

        String calendarType = calendar.getCalendarType();

        Map<String, String> params = new HashMap<>();

        switch (calendarType) {
            case "EDT":
                strategyId = 2;
                params.put("groupId", calendar.getParam());
                break;
            case "GOOGLE_CALENDAR":
                strategyId = 3;
                params.put("gCalId", calendar.getParam());
                params.put("tokenRequest", token);

                params.put("apiId", ConstantAPI.API_ID);
                params.put("apiSecret", ConstantAPI.API_SECRET);
                break;
            default:
                strategyId = 1; // URL of ICS file
                params.put("url", calendar.getParam());
                break;
        }
        CalendarStrategy strategy = CalendarFactory.getInstance().get(strategyId);
		
		// Get ICS file and first event of the day
		CalendarEvent nextEvent = null;
		nextEvent = strategy.getFirstEvent(calendar.getParam(), date, params);
			
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
