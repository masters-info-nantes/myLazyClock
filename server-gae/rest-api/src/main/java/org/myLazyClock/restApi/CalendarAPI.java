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
        list.add(c1);
        Calendar c2 = new Calendar();
        c2.setCalendarType("ICS_FILE");
        c2.setName("Cours Escalade");
        c2.setDefaultEventLocation("Gymnase grandmont tours");
        c2.setParam("http://intranet.ect37.com/admin/2014/lessons/Perfectionnement%20%2812%20-%2017%20ans%29/calendar/ics/");
        c2.setTravelMode("DRIVING");
        list.add(c2);
        return list;
    }

    @ApiMethod(name = "calendar.update", httpMethod = ApiMethod.HttpMethod.PUT, path="calendar")
    public Calendar update(@Named("alarmClockId") String alarmClockId, Calendar calendar, User user) {
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
    public void delete(@Named("alarmClockId") String alarmClockId, @Named("calendarId") String calendarId, User user) {

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
