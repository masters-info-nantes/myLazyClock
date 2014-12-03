package org.myLazyClock.services;

import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.bean.AlarmClockEvent;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.model.repository.CalendarRepository;
import org.myLazyClock.travelApi.TravelDuration;
import org.myLazyClock.travelApi.TravelFactory;
import org.myLazyClock.travelApi.TravelStrategy;

import java.util.*;

/**
 * Created on 30/11/14.
 *
 * @author jeremy
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
                    if (cal.isUseAlwaysDefaultLocation()) {
                        eventOfDay.setAddress(cal.getDefaultEventLocation());
                    } else if (eventOfDay.getAddress() == null || eventOfDay.equals("")) {
                        eventOfDay.setAddress(cal.getDefaultEventLocation());
                    }
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
                alarmEvent.setTravelDuration(0l);
            }
            else {
                CalendarEvent soonerEvent = Collections.min(eventsInDay);
                alarmEvent.setBeginDate(soonerEvent.getBeginDate());
                alarmEvent.setName(soonerEvent.getName());
                alarmEvent.setAddress(soonerEvent.getAddress());
                alarmEvent.setTravelDuration(getDuration(alarmClock, alarmEvent.getAddress(), soonerEvent.getBeginDate()));
            }

            eventsInWeek.add(alarmEvent);

            // Next day
            currentCal.setTimeInMillis(currentCal.getTimeInMillis() + (24 * 60 * 60 * 1000));
        }

        return eventsInWeek;
    }

    private Long getDuration (AlarmClock alarmClock, String to, Date when) {
        TravelStrategy strategy = null;

        strategy = TravelFactory.getInstance().get(1);

        Map<String, String> params = new HashMap<String, String>();

        TravelDuration travel = strategy.getDuration(alarmClock.getAddress(), to, when, params);


        return travel.getTimeMin();
    }
}
