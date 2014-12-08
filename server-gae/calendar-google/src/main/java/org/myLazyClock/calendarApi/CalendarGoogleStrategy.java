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
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

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
    public CalendarEvent getFirstEvent(String url, java.util.Calendar day, Map<String, String> params) throws EventNotFoundException {
        if (day == null || params == null || params.get("tokenRequest") == null || params.get("tokenRequest").equals("")) {
            throw new EventNotFoundException();
        }

        CalendarEvent returnEvent = new CalendarEvent();

        try {

            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            String emailAddress = "1072024627812-1be21gic6j8vcudtcst04al9lbdo4516@developer.gserviceaccount.com";

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId(emailAddress)
                    .setServiceAccountScopes(Arrays.asList(CalendarScopes.CALENDAR_READONLY))
                    .setServiceAccountPrivateKeyFromP12File(new File("WEB-INF/myLazyClock.p12"))
                    .build().setRefreshToken(params.get("tokenRequest"));

            Calendar service = new Calendar.Builder(httpTransport, jsonFactory, null)
                    .setApplicationName("myLazyClock")
                    .setHttpRequestInitializer(credential).build();


            Events events = service.events().list(params.get("gCalId")).execute();
                    //.setTimeMin(new DateTime(day.getTime())).setOrderBy("startTime").execute();

            if (events.getItems().isEmpty()) {
                throw new EventNotFoundException();
            }

            Event event = events.getItems().get(0);

            returnEvent.setName(event.getSummary());
            returnEvent.setBeginDate(new Date(event.getStart().getDateTime().getValue()));
            returnEvent.setEndDate(new Date(event.getEnd().getDateTime().getValue()));
            returnEvent.setAddress(event.getLocation());

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    	return returnEvent;
    }
}
