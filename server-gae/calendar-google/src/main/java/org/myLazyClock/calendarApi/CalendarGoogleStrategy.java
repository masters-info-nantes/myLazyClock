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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.myLazyClock.calendarApi.exception.EventNotFoundException;
import org.myLazyClock.calendarApi.exception.ForbiddenCalendarException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Map;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarGoogleStrategy implements CalendarStrategy {

    public static final CalendarId ID = CalendarId.GOOGLE_CALENDAR;

    @Override
    public CalendarId getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar Google";
    }

    /**
     * Find the first event before 24h before endDay in google calendar in params
     * @param params Need some params : <br/>
     *  - <strong> tokenRequest </strong> content the google refreshToken of user <br/>
     *  - <strong> gCalId </strong> content the id of calendar <br/>
     *  - <strong> apiId </strong> and <strong> apiSecret </strong> generate by maven in services.ConstantAPI <br/>
     *
     * @param beginDate Lower bound date in which search event
     * @param endDate Upper bound date in which search event
     *
     * @return CalendarEvent the first event find
     * @throws org.myLazyClock.calendarApi.exception.EventNotFoundException if not event found in specific day
     */
    @Override
    public CalendarEvent getFirstEvent(Map<String, String> params, java.util.Calendar beginDate, java.util.Calendar endDate) throws EventNotFoundException, ForbiddenCalendarException {
        if (beginDate == null || endDate == null || params == null
                || params.get("tokenRequest") == null || params.get("tokenRequest").equals("")
                || params.get("gCalId") == null || params.get("gCalId").equals("")) {
            throw new EventNotFoundException();
        }

        DateTime startTime = new DateTime(beginDate.getTime());
        DateTime endTime = new DateTime(endDate.getTime());

        CalendarEvent returnEvent = new CalendarEvent();

        try {

            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets(params.get("apiId"),params.get("apiSecret"))
                    .build().setRefreshToken(params.get("tokenRequest"));

            Calendar service = new Calendar.Builder(httpTransport, jsonFactory, null)
                    .setApplicationName("myLazyClock")
                    .setHttpRequestInitializer(credential).build();


            Events events = service.events().list(params.get("gCalId"))
                    .setTimeMin(startTime)
                    .setTimeMax(endTime)
                    .setOrderBy("startTime")
                    .setSingleEvents(true).execute();

            if (events.getItems().isEmpty()) {
                throw new EventNotFoundException();
            }

            Event event = events.getItems().get(0);

            returnEvent.setName(event.getSummary());
            returnEvent.setBeginDate(new Date(event.getStart().getDateTime().getValue()));
            returnEvent.setEndDate(new Date(event.getEnd().getDateTime().getValue()));
            returnEvent.setAddress(event.getLocation());

        } catch (GeneralSecurityException e) {
            throw new ForbiddenCalendarException("Accés interdit au calendrier de Google");
        } catch (IOException e) {
            throw new EventNotFoundException(e);
        }

    	return returnEvent;
    }
}
