package org.myLazyClock.restApi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.CalendarStrategy;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.services.CalendarModulesService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @ApiMethod(name = "calendar.listAll", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/list")
    public Collection<CalendarStrategy> getAll() {
        return CalendarModulesService.getInstance().listModule();
    }


    /*
     * To do :
     * - calendar scope not work for me
     * - include Event object to remplace AlarmClock
     */
    @ApiMethod(name = "calendar.firstEvent", httpMethod = ApiMethod.HttpMethod.GET, path="calendar/firstEvent")
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
    }
}
