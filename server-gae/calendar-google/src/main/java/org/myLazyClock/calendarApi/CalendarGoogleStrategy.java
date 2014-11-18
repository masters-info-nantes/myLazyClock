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

package org.myLazyClock.calendarApi;

import java.util.Calendar;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarGoogleStrategy implements CalendarStrategy {

    public static final int ID = 3;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar Google";
    }
    
    @Override
    public CalendarEvent getFirstEvent(String url, Calendar day) throws EventNotFoundException {
        if (day == null) {
            throw new EventNotFoundException();
        }

        CalendarEvent returnEvent = new CalendarEvent();

        returnEvent.setBeginDate(day.getTime());

    	return returnEvent;
    }
}
