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

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;
import org.myLazyClock.calendarApi.exception.EventNotFoundException;
import org.myLazyClock.calendarApi.exception.ForbiddenCalendarException;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

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

        BufferedReader reader = new BufferedReader(new InputStreamReader(edtUrl.openStream()));

        String line;
        StringBuilder page = new StringBuilder("");

        while ((line = reader.readLine()) != null) {
            page.append(line);
            page.append("\r\n");
        }
        reader.close();

        return page.toString();

    }

    /**
     * Construct calendar from given ICS file
     * and return first event of given day
     * @param params Need <strong>url</strong> who contain the url of ics file
     *
     * @param beginDate Lower bound date in which search event
     * @param endDate Upper bound date in which search event
     *
     * @return First event of the day or null if no events
     * @throws org.myLazyClock.calendarApi.exception.EventNotFoundException if no event found
     */
    @Override
    public CalendarEvent getFirstEvent (Map<String, String> params, java.util.Calendar beginDate, java.util.Calendar endDate) throws EventNotFoundException, ForbiddenCalendarException {

        InputStream is;

        // use Memcache
        MemcacheService cache = MemcacheServiceFactory.getMemcacheService("calendarICS");
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.WARNING));
        byte[] icsValue = (byte[]) cache.get(params.get("url"));

        if (icsValue == null) {
            String icsFile;
            try {
                icsFile = getEdt(new URL(params.get("url")));
                if (icsFile == null) {
                    throw new ForbiddenCalendarException("Le fichier ICS n'a pas était trouvé");
                }

                cache.put(params.get("url"), icsFile.getBytes(), Expiration.byDeltaSeconds(7200));

                is = new ByteArrayInputStream(icsFile.getBytes());
            } catch (IOException e) {
                throw new ForbiddenCalendarException(e);
            }

        } else {
            is = new ByteArrayInputStream(icsValue);
        }

        // Construct calendar from ICS file
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar;
        try {
            calendar = builder.build(is);
        } catch (IOException | ParserException e) {
            throw new ForbiddenCalendarException(e);
        }

        // Looking for first event of the day
        ComponentList events = calendar.getComponents(Component.VEVENT);
        VEvent nextEvent = null;

        for(Object event: events){
            VEvent currEvent = (VEvent)event;

            java.util.Calendar calCurr = java.util.Calendar.getInstance();
            calCurr.setTime(currEvent.getStartDate().getDate());

            // Check if beginDate <= calCurr <= endDate
            if(beginDate.compareTo(calCurr) <= 0 && endDate.compareTo(calCurr) >= 0) {
                // Test if no event found
                boolean currentIsBefore;
                if(nextEvent == null){
                    currentIsBefore = calCurr.before(endDate);
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
