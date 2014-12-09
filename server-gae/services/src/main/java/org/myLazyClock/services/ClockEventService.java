package org.myLazyClock.services;

import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.model.model.MyLazyClockUser;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.model.repository.CalendarRepository;
import org.myLazyClock.services.bean.AlarmClockEvent;
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

    public Collection<AlarmClockEvent> listEventForWeek(String alarmClockId){
        AlarmClock alarmClock = AlarmClockRepository.getInstance().findOne(Long.decode(alarmClockId));
        MyLazyClockUser user = MyLazyClockUserService.getInstance().findOne(alarmClock.getUser());

        String userToken = (user != null) ? user.getToken() : "";

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

            ArrayList<AlarmClockEvent> eventsInDay = new ArrayList<AlarmClockEvent>();
            for(Calendar cal: calendarList){
                try {

                    CalendarEvent eventOfDay = serviceCalendar.getFirstEventOfDay(cal, currentCal, userToken);
                    if (cal.isUseAlwaysDefaultLocation()) {
                        eventOfDay.setAddress(cal.getDefaultEventLocation());
                    } else if (eventOfDay.getAddress() == null || eventOfDay.equals("")) {
                        eventOfDay.setAddress(cal.getDefaultEventLocation());
                    }
                    AlarmClockEvent alarmClockEventOfDay = calendarEventToAlarmClockEvent(eventOfDay);
                    alarmClockEventOfDay.setTravelMode(cal.getTravelMode());

                    eventsInDay.add(alarmClockEventOfDay);
                }
                catch(EventNotFoundException e){
                    // No event for this day in this calendar
                }
            }

            // Add sooner event of day in list
            AlarmClockEvent alarmEvent = null;

            if(!eventsInDay.isEmpty()){
                alarmEvent = Collections.min(eventsInDay);
                alarmEvent.setTravelDuration(
                        getDuration(alarmClock, alarmEvent)
                );

                eventsInWeek.add(alarmEvent);
            }

            // Next day
            currentCal.setTimeInMillis(currentCal.getTimeInMillis() + (24 * 60 * 60 * 1000));
        }

        return eventsInWeek;
    }

    private AlarmClockEvent calendarEventToAlarmClockEvent(CalendarEvent calendarEvent) {
        AlarmClockEvent event = new AlarmClockEvent();

        event.setBeginDate(calendarEvent.getBeginDate());
        event.setEndDate(calendarEvent.getEndDate());

        event.setName(calendarEvent.getName());
        event.setAddress(calendarEvent.getAddress());

        return event;
    }

    private Long getDuration (AlarmClock alarmClock, AlarmClockEvent event) {
        TravelStrategy strategy;

        Map<String, String> params = new HashMap<String, String>();

        switch (event.getTravelMode()) {
            case BICYCLING:
                strategy = TravelFactory.getInstance().get(1);
                params.put("mode", "bicycling");
                break;
            case TRANSIT:
                strategy = TravelFactory.getInstance().get(1);
                params.put("mode", "transit");
                break;
            case WALKING:
                strategy = TravelFactory.getInstance().get(1);
                params.put("mode", "walking");
                break;

            case DRIVING:
            default:
                strategy = TravelFactory.getInstance().get(1);
        }

        TravelDuration travel = strategy.getDuration(alarmClock.getAddress(), event.getAddress(), event.getBeginDate(), params);

        return travel.getTime();
    }
}
