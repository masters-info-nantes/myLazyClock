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

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import org.myLazyClock.calendarApi.JsonConverter;

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

        HashMap<String, String> urlParameters = new HashMap<String, String>();
        urlParameters.put("getWeek", url);
        urlParameters.put("year", String.valueOf(day.get(Calendar.YEAR)));
        urlParameters.put("week", String.valueOf(day.get(Calendar.WEEK_OF_YEAR) - 1));

        CalendarEvent returnEvent = new CalendarEvent();

        try {
            // Calendar days begin from sunday 1 to saturday 7
            // Smart edt array begin from monday 0 to sunday 6
            int dayIndex = day.get(Calendar.DAY_OF_WEEK);
            dayIndex = (dayIndex == 1) ? 6 : dayIndex - 2;

            JsonElement root = JsonConverter.getJson(urlParameters).getAsJsonArray().get(0);
            JsonArray daysArray = root.getAsJsonObject().get("days").getAsJsonArray();
            JsonArray modulesArray = daysArray.get(dayIndex).getAsJsonObject().get("modules").getAsJsonArray();

            if(modulesArray.size() <= 0 ){ // No events today
                throw new EventNotFoundException();
            }

            JsonObject firstModule = modulesArray.get(0).getAsJsonObject();
            returnEvent.setName("[SmartEdt]" + firstModule.get("name").getAsString());

            // StartTime is number of minutes from midnight
            int startHour = firstModule.get("startTime").getAsInt();

            Calendar beginDate = (Calendar) day.clone();
            beginDate.set(Calendar.HOUR_OF_DAY, startHour / 60);
            beginDate.set(Calendar.MINUTE, startHour % 60);

            returnEvent.setBeginDate(beginDate.getTime());
        }
        catch(IOException ex){
            throw new EventNotFoundException();
        }

        return returnEvent;
    }
}
