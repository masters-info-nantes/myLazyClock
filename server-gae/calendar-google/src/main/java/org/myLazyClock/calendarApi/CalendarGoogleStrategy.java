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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

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

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    jsonFactory,
                    "1072024627812-kgv1uou2btdphtvb2l2bbh14n6u2n2mg.apps.googleusercontent.com",
                    "K645ivQD06Ll2ELmhN9zlGU_",
                    Arrays.asList(CalendarScopes.CALENDAR_READONLY)
            ).build();
            GoogleTokenResponse response=flow.newTokenRequest(params.get("tokenRequest")).setRedirectUri("http://localhost").execute();


            Credential credential = flow.createAndStoreCredential(response, null);

            Calendar service = new Calendar.Builder(httpTransport, jsonFactory, null)
                    .setApplicationName("myLazyClock")
                    .setHttpRequestInitializer(credential).build();


            Events events = service.events().list(params.get("gCalId")).execute();
                    //.setTimeMin(new DateTime(day.getTime())).setOrderBy("startTime").execute();
            Event event = events.getItems().get(0);

            returnEvent.setName(event.getSummary());
            returnEvent.setBeginDate(new Date(event.getStart().getDate().getValue()));
            returnEvent.setEndDate(new Date(event.getEnd().getDate().getValue()));
            returnEvent.setAddress(event.getLocation());

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    	return returnEvent;
    }
}
