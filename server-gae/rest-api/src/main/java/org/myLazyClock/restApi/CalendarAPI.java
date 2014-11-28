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

package org.myLazyClock.restApi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.CalendarStrategy;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.services.CalendarModulesService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Jeremy on 15/11/14.
 */

@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class CalendarAPI {

    @ApiMethod(name = "calendar.list", httpMethod = ApiMethod.HttpMethod.GET, path="calendar")
    public Collection<Calendar> list(@Named("alarmClockId") String alarmClockId, User user) {
        ArrayList<Calendar> list = new ArrayList<Calendar>();
        Calendar c1 = new Calendar();
        c1.setCalendarType("GOOGLE_CALENDAR");
        c1.setName("Anniversaires");
        c1.setDefaultEventLocation("j'habite ici à nantes");
        c1.setParam("#contacts@group.v.calendar.google.com");
        c1.setTravelMode("BICYCLING");
        c1.setId(Long.decode("1"));
        list.add(c1);
        Calendar c2 = new Calendar();
        c2.setCalendarType("ICS_FILE");
        c2.setName("Cours Escalade");
        c2.setDefaultEventLocation("Gymnase grandmont tours");
        c2.setParam("http://intranet.ect37.com/admin/2014/lessons/Perfectionnement%20%2812%20-%2017%20ans%29/calendar/ics/");
        c2.setTravelMode("DRIVING");
        c2.setId(Long.decode("2"));
        list.add(c2);
        Calendar c3 = new Calendar();
        c3.setCalendarType("EDT");
        c3.setName("UFR Sciences et Techniques - M1ALMA-GL");
        c3.setDefaultEventLocation("UFR Sciences et Techniques Nantes");
        c3.setParam("906");
        c3.setTravelMode("DRIVING");
        c3.setId(Long.decode("3"));
        list.add(c3);
        return list;
    }

    @ApiMethod(name = "calendar.update", httpMethod = ApiMethod.HttpMethod.PUT, path="calendar")
    public Calendar update(Calendar calendar, User user) {
        return new Calendar();
    }

    @ApiMethod(name = "calendar.add", httpMethod = ApiMethod.HttpMethod.POST, path="calendar")
    public Calendar add(@Named("alarmClockId") String alarmClockId, Calendar calendar, User user) {
        System.out.println(alarmClockId);
        System.out.println(calendar.getName());
        System.out.println(calendar.getParam());
        System.out.println(calendar.getTravelMode());
        return calendar;
    }

    @ApiMethod(name = "calendar.delete", httpMethod = ApiMethod.HttpMethod.DELETE, path="calendar")
    public void delete(@Named("calendarId") String calendarId, User user) {

    }

    @ApiMethod(name = "calendar.item", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/item")
    public Calendar item(@Named("calendarId") String calendarId, User user) {
        Calendar c1 = new Calendar();
        c1.setCalendarType("GOOGLE_CALENDAR");
        c1.setName("Anniversaires");
        c1.setDefaultEventLocation("j'habite ici à nantes");
        c1.setParam("#contacts@group.v.calendar.google.com");
        c1.setTravelMode("BICYCLING");
        c1.setId(Long.decode("1"));
        return c1;
    }

    /*@ApiMethod(name = "calendar.listAll", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/list")
    public Collection<CalendarStrategy> getAll() {
        return CalendarModulesService.getInstance().listModule();
    }*/


    /*
     * To do :
     * - calendar scope not work for me
     * - include Event object to remplace AlarmClock
     */
    /*@ApiMethod(name = "calendar.firstEvent", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/firstEvent")
    public CalendarEvent getFirstEvent(@Named("cal") String calendarStrategy, @Named("details") String calendarDetails,
                                       @Named("day") String day, User user) throws ParseException, EventNotFoundException, OAuthRequestException {

        CalendarModulesService serviceCalendar = CalendarModulesService.getInstance();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        DateFormat dayEvent = new SimpleDateFormat("dd/MM/yyyy");

        cal.setTime(dayEvent.parse(day));	// No offset (month+1) so no offset in getFirstEvent to compare

        // To search any event before this one
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);

        // Get first event of the day
        int strategy = (calendarStrategy.equals("google")) ? 3 : 2;

        CalendarEvent nextEvent = serviceCalendar.getFirstEventOfDay(strategy, cal);

        if(user == null){
            throw new OAuthRequestException("Must be connected to access your calendar");
        }

        return nextEvent;
    }*/
}
