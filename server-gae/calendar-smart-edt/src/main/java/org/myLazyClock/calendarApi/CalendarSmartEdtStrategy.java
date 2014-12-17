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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.myLazyClock.calendarApi.exception.EventNotFoundException;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    private Map<Integer, SmartEdtGroupData> groupData;

    public CalendarSmartEdtStrategy(){
        this.groupData = new HashMap<>();
    }

    /**
     * Find First event from SmartEDT for specific group id
     * @param params Need <strong>groupId</strong> who contain the id of the group in smart edt
     *
     * @param beginDate Lower bound date in which search event
     * @param endDate Upper bound date in which search event
     *
     * @return First event of the day or null if no events
     * @throws org.myLazyClock.calendarApi.exception.EventNotFoundException if no event found
     */
    @Override
    public CalendarEvent getFirstEvent(Map<String,String> params, java.util.Calendar beginDate, java.util.Calendar endDate) throws EventNotFoundException {

        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("getWeek", params.get("groupId"));
        urlParameters.put("year", String.valueOf(beginDate.get(Calendar.YEAR)));
        urlParameters.put("week", String.valueOf(beginDate.get(Calendar.WEEK_OF_YEAR) - 1));

        CalendarEvent returnEvent = new CalendarEvent();

        try {
            // Calendar days begin from sunday 1 to saturday 7
            // Smart edt array begin from monday 0 to sunday 6
            int dayIndex = beginDate.get(Calendar.DAY_OF_WEEK);
            dayIndex = (dayIndex == 1) ? 6 : dayIndex - 2;

            JsonElement root = JsonConverter.getJson(urlParameters).getAsJsonArray().get(0);
            JsonArray daysArray = root.getAsJsonObject().get("days").getAsJsonArray();
            JsonArray modulesArray = daysArray.get(dayIndex).getAsJsonObject().get("modules").getAsJsonArray();

            if(modulesArray.size() <= 0 ){ // No events today
                throw new EventNotFoundException();
            }

            JsonObject event = modulesArray.get(0).getAsJsonObject();
            String eventName = this.buildEventName(Integer.parseInt(params.get("groupId")), event);
            returnEvent.setName(eventName);

            // StartTime is number of minutes from midnight
            int startHour = event.get("startTime").getAsInt();

            Calendar beginEventDate = (Calendar) beginDate.clone();
            beginEventDate.set(Calendar.HOUR_OF_DAY, startHour / 60);
            beginEventDate.set(Calendar.MINUTE, startHour % 60);

            returnEvent.setBeginDate(beginEventDate.getTime());
        }
        catch(IOException ex){
            throw new EventNotFoundException();
        }

        return returnEvent;
    }

    private String buildEventName(int groupId, JsonElement event){

        // Get group details
        SmartEdtGroupData group = this.groupData.get(groupId);
        if(group == null){
            group = new SmartEdtGroupData(groupId);
            try {
                group.reloadData();
            } catch (IOException e) {
                return "[SmartEDT] No details about course";
            }
        }

        // Build event name with group data
        JsonArray rooms = event.getAsJsonObject()
                               .get("rooms")
                               .getAsJsonArray();

        Integer courseName = event.getAsJsonObject()
                                  .get("name")
                                  .getAsInt();

        StringBuilder eventName = new StringBuilder("");
        eventName.append(
            (rooms.size() > 0)
                ? group.getData(GroupDataType.ROOM, rooms.get(0).getAsInt())
                : ""
        );

        eventName.append(
            (courseName != null && courseName != -1)
                ? group.getData(GroupDataType.COURSE_NAME, courseName)
                : ""
        );

        // In some special cases (as internships) event has no teacher, no room and course name, category equals -1
        return (eventName.length() > 0)
                    ? eventName.toString()
                    : event.getAsJsonObject().get("notes").getAsString()
        ;
    }
}
