package org.myLazyClock.restApi;

/**
 * Created by Maxime on 17/11/14.
 */

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.CalendarStrategy;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.AlarmClockEvent;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.model.repository.CalendarRepository;
import org.myLazyClock.services.CalendarModulesService;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class ClockEventAPI {

    @ApiMethod(name = "clockevent.list", httpMethod = ApiMethod.HttpMethod.GET, path="clockevent")
    public Collection<AlarmClockEvent> list(@Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException, NotFoundException {

        // Retrieve alarm and calendars
        AlarmClock alarm = AlarmClockRepository.getInstance().findOne(alarmClockId);
        Collection<org.myLazyClock.model.model.Calendar> calendarList = CalendarRepository.getInstance().findAll(alarm);

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris")); // For new Date()
        java.util.Calendar currentCal = java.util.Calendar.getInstance();
        currentCal.setTime(new Date());

        // To search any event before this one
        currentCal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        currentCal.set(java.util.Calendar.MINUTE, 59);

        CalendarModulesService serviceCalendar = CalendarModulesService.getInstance();
        Collection<AlarmClockEvent> eventsInWeek = new ArrayList<AlarmClockEvent>();

        // Search first event for each day of week
        for(int i = 0; i < 7; i++){

            ArrayList<CalendarEvent> eventsInDay = new ArrayList<CalendarEvent>();
            for(org.myLazyClock.model.model.Calendar cal: calendarList){
                try {
                    CalendarEvent eventOfDay = serviceCalendar.getFirstEventOfDay(cal, currentCal);
                    eventsInDay.add(eventOfDay);
                }
                catch(EventNotFoundException e){
                    // No event for this day in this calendar
                }
            }

            // Add sooner event of day in list
            AlarmClockEvent alarmEvent = new AlarmClockEvent();

            if(eventsInDay.isEmpty()){
                alarmEvent.setBeginDate(currentCal.getTime());
                alarmEvent.setName("No event today");
                alarmEvent.setAddress("Default Address");
                alarmEvent.setTravelDuration(new Long(6876));
            }
            else {
                CalendarEvent soonerEvent = Collections.min(eventsInDay);
                alarmEvent.setBeginDate(soonerEvent.getBeginDate());
                alarmEvent.setName(soonerEvent.getName());
                alarmEvent.setAddress("Default Address");
                alarmEvent.setTravelDuration(new Long(6876));
            }

            eventsInWeek.add(alarmEvent);

            // Next day
            currentCal.setTimeInMillis(currentCal.getTimeInMillis() + (24 * 60 * 60 * 1000));
        }

        return eventsInWeek;
    }
}
