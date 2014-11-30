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

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.*;
import java.net.URL;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarIcsStrategy implements CalendarStrategy {

    public static final int ID = 1;
    //private HashMap<String, String> icsFiles;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar ICS";
    }

    public CalendarIcsStrategy(){
        //    this.icsFiles = new HashMap<String, String>();
    }

    /**
     * Get ICS file on the given server
     * @param edtUrl Path to get ics file in schedule
     * @return ICS file as string
     * @throws java.io.IOException
     *
     * @warning Not working with science schedule because of login but
     * 		  working well with staps
     */
    private String getEdt(URL edtUrl) throws IOException {

        /*
        if(this.icsFiles.containsKey(edtUrl.toString())){
            return this.icsFiles.get(edtUrl.toString());
        }
        */

        BufferedReader reader = new BufferedReader(new InputStreamReader(edtUrl.openStream()));

        String line;
        StringBuilder page = new StringBuilder("");

        while ((line = reader.readLine()) != null) {
            page.append(line);
            page.append("\r\n");
        }
        reader.close();

        //this.icsFiles.put(edtUrl.toString(), page.toString());
        return page.toString();

    }

    /**
     * Construct calendar from given ICS file
     * and return first event of given day
     * @param url the ur of ics file
     * @param day day in which search event
     * @return First event of the day or null if no events
     * @throws EventNotFoundException if no event found
     */
    @Override
    public CalendarEvent getFirstEvent (String url, java.util.Calendar day) throws EventNotFoundException {
        String icsFile = null;
        try {
            icsFile = getEdt(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Construct calendar from ICS file
        InputStream is = new ByteArrayInputStream(icsFile.getBytes());
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar = null;
        try {
            calendar = builder.build(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        // Looking for first event of the day
        ComponentList events = calendar.getComponents(Component.VEVENT);
        VEvent nextEvent = null;

        for(Object event: events){
            VEvent currEvent = (VEvent)event;

            java.util.Calendar calCurr = java.util.Calendar.getInstance();
            calCurr.setTime(currEvent.getStartDate().getDate());

            boolean sameDay = calCurr.get(java.util.Calendar.YEAR) == day.get(java.util.Calendar.YEAR) &&
                    calCurr.get(java.util.Calendar.DAY_OF_YEAR) == day.get(java.util.Calendar.DAY_OF_YEAR);

            if(sameDay){
                // Test if no event found
                boolean currentIsBefore = false;
                if(nextEvent == null){
                    currentIsBefore = calCurr.before(day);
                }
                else {
                    java.util.Calendar calNext = java.util.Calendar.getInstance();
                    calNext.setTime(nextEvent.getStartDate().getDate());
                    currentIsBefore = calCurr.before(calNext);
                }

                if(currentIsBefore){
                    nextEvent = currEvent;
                }
            }
        }

        if (nextEvent == null) {
            throw new EventNotFoundException();
        }

        CalendarEvent eventReturn = new CalendarEvent();

        eventReturn.setBeginDate(nextEvent.getStartDate().getDate());
        eventReturn.setEndDate(nextEvent.getEndDate().getDate());

        eventReturn.setName(nextEvent.getSummary().getValue());

        return eventReturn;
    }
}
