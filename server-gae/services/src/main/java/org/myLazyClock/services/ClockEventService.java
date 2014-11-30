package org.myLazyClock.services;

import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.AlarmClockEvent;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.model.repository.CalendarRepository;

import java.util.*;

/**
 * Created by jeremy on 30/11/14.
 */
public class ClockEventService {
    private static ClockEventService service = null;

    private ClockEventService() {}

    public static synchronized ClockEventService getInstance() {
        if (null == service) {
            service = new ClockEventService();
        }
        return service;
    }

    public Collection<AlarmClockEvent> listEventForWeek(AlarmClock alarmClock){
        Collection<Calendar> calendarList = CalendarRepository.getInstance().findAll(alarmClock);

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
