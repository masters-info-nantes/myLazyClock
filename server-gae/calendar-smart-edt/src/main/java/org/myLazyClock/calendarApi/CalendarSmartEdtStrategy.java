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
* Send request to get schedule and returns the first
* event of the given day
*  
* @author Jeremy
* 
* @warning Caution with offsets and calendar and see getEdt warning
* 
* External library iCal4j (http://build.mnode.org/projects/ical4j/apidocs/): 
*   - put in eclipse projet (add jar) for local use
*   - in war/WEB-INF/lib to deploy
*   
*/
public class CalendarSmartEdtStrategy implements CalendarStrategy {

    public static final int ID = 2;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar edt univ-nantes";
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
