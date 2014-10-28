package org.myLazyClock.services;

import org.myLazyClock.calendarApi.CalendarFactory;
import org.myLazyClock.calendarApi.CalendarStrategy;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarModulesService {

    public String listModule() {
        String result = "";

        CalendarFactory factory = CalendarFactory.getInstance();

        for ( CalendarStrategy strategy : factory) {
            result += strategy.getName() + " : " + strategy.getId() + "\n";
        }

        return result;
    }
}
