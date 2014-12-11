package org.myLazyClock.services;

import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.CalendarFactory;
import org.myLazyClock.calendarApi.CalendarStrategy;
import org.myLazyClock.calendarApi.exception.EventNotFoundException;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.Calendar;
import org.myLazyClock.model.model.MyLazyClockUser;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.model.repository.MyLazyClockUserRepository;
import org.myLazyClock.services.bean.AlarmClockEvent;
import org.myLazyClock.travelApi.TravelDuration;
import org.myLazyClock.travelApi.TravelFactory;
import org.myLazyClock.travelApi.TravelStrategy;
import org.myLazyClock.travelApi.exception.TravelNotFoundException;

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
        MyLazyClockUser user = MyLazyClockUserRepository.getInstance().findOne(alarmClock.getUser());

        String userToken = (user != null) ? user.getToken() : "";

        Collection<Calendar> calendarList = alarmClock.getCalendars();

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris")); // For new Date()
        java.util.Calendar currentCal = java.util.Calendar.getInstance();
        currentCal.setTime(new Date());

        // To search any event before this one
        currentCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        currentCal.set(java.util.Calendar.MINUTE, 0);

        Collection<AlarmClockEvent> eventsInWeek = new ArrayList<>();

        // Search first event for each day of week
        for(int i = 0; i < 7; i++){

            CalendarEvent eventInDay = null;
            Calendar calOfEvent = null;

            for(Calendar cal: calendarList){
                try {

                    CalendarEvent eventOfDay = getFirstEventOfDay(cal, currentCal, userToken);
                    if (cal.isUseAlwaysDefaultLocation() || eventOfDay.getAddress() == null || eventOfDay.getAddress().equals("")) {
                        eventOfDay.setAddress(cal.getDefaultEventLocation());
                    }

                    if (eventInDay != null) {
                        if (eventOfDay.compareTo(eventInDay) < 0) {
                            eventInDay = eventOfDay;
                            calOfEvent = cal;
                        }
                    } else {
                        eventInDay = eventOfDay;
                        calOfEvent = cal;
                    }
                }
                catch(EventNotFoundException e){
                    // No event for this day in this calendar
                }
            }

            AlarmClockEvent alarmEvent;

            if(eventInDay != null){
                alarmEvent = calendarEventToAlarmClockEvent(eventInDay);
                alarmEvent.setTravelMode(calOfEvent.getTravelMode());

                try {
                    alarmEvent.setTravelDuration(
                            getDuration(alarmClock, alarmEvent)
                    );
                } catch (TravelNotFoundException e) {
                    e.printStackTrace();
                    alarmEvent.setTravelDuration(0l);
                }

                eventsInWeek.add(alarmEvent);
            }

            // Next day
            currentCal.add(java.util.Calendar.DATE, 1);
        }

        return eventsInWeek;
    }

    /**
     * Return the first event in specified day on online schedule
     *
     * @param calendar Calendar to use with settings (strategy, url)
     * @param date Day where to search event
     * @return First event of day if exist, null otherwise
     */

    public CalendarEvent getFirstEventOfDay(Calendar calendar, java.util.Calendar date, String token) throws EventNotFoundException{

        java.util.Calendar endDate = (java.util.Calendar) date.clone();
        endDate.set(java.util.Calendar.HOUR_OF_DAY, 23);
        endDate.set(java.util.Calendar.MINUTE, 59);

        int strategyId; // ICS_FILE

        String calendarType = calendar.getCalendarType();

        Map<String, String> params = new HashMap<>();

        switch (calendarType) {
            case "EDT":
                strategyId = 2;
                params.put("groupId", calendar.getParam());
                break;
            case "GOOGLE_CALENDAR":
                strategyId = 3;
                params.put("gCalId", calendar.getParam());
                params.put("tokenRequest", token);

                params.put("apiId", ConstantAPI.API_ID);
                params.put("apiSecret", ConstantAPI.API_SECRET);
                break;
            default:
                strategyId = 1; // URL of ICS file
                params.put("url", calendar.getParam());
                break;
        }

        CalendarStrategy strategy = CalendarFactory.getInstance().get(strategyId);

        return strategy.getFirstEvent(params, date, endDate);
    }

    private AlarmClockEvent calendarEventToAlarmClockEvent(CalendarEvent calendarEvent) {
        AlarmClockEvent event = new AlarmClockEvent();

        event.setBeginDate(calendarEvent.getBeginDate());
        event.setEndDate(calendarEvent.getEndDate());

        event.setName(calendarEvent.getName());
        event.setAddress(calendarEvent.getAddress());

        return event;
    }

    private Long getDuration (AlarmClock alarmClock, AlarmClockEvent event) throws TravelNotFoundException {
        TravelStrategy strategy;

        Map<String, String> params = new HashMap<>();

        switch (event.getTravelMode()) {
            case BICYCLING:
                strategy = TravelFactory.getInstance().get(1);
                params.put("mode", "bicycling");
                break;
            case TRANSIT:
                strategy = TravelFactory.getInstance().get(2);
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
