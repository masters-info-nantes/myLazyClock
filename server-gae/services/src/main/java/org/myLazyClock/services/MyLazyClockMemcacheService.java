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

package org.myLazyClock.services;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import org.myLazyClock.services.bean.AlarmClockBean;
import org.myLazyClock.services.bean.CalendarBean;
import org.myLazyClock.services.bean.EdtData;
import org.myLazyClock.services.bean.MyLazyClockUserValid;

import java.util.Collection;
import java.util.logging.Level;

/**
 * Centralize all access on read or on write into the memcache store,
 *  with a good management of namespace and clean cache cascade
 *
 * @author dralagen
 */
public class MyLazyClockMemcacheService {

    private static String NAMESPACE_ALARM_CLOCK = "alarmClock";
    private static String NAMESPACE_CALENDAR = "calendar";
    private static String NAMESPACE_USER_VALIDITY = "myLazyClockUserCheckToken";
    private static String NAMESPACE_SMART_EDT = "smartEDT";

    private static MyLazyClockMemcacheService instance = null;

    /**
     * Get the instance of google Memcache
     *
     * @return the instance of google Memcache
     */
    public static MyLazyClockMemcacheService getInstance() {
        if (instance == null) {
            instance = new MyLazyClockMemcacheService();
        }
        return instance;
    }

    private MyLazyClockMemcacheService() {
        instance = this;
    }


    /**
     * Get a Memcache service for specific namespace
     *
     * @param namespace namespace of memcache
     * @return the Memcache
     */
    private MemcacheService getService(String namespace) {
        MemcacheService cache = MemcacheServiceFactory.getMemcacheService("alarmClock");
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.WARNING));
        return cache;
    }

    /**
     * Add in cache, the value who can access by the key on the same namespace
     *
     * @param namespace The Namespace use to store the cache entry
     * @param key  key of the new entry, key must be Serializable
     * @param value value for the new entry, value must be Serializable
     * @param expires The time-based Expiration, or null for none
     */
    private void add(String namespace, Object key, Object value, Expiration expires) {
        MemcacheService cache = getService(namespace);
        try {
            cache.put(key, value, expires);
        } catch (Exception ignore) {}
    }

    /**
     * A convenience shortcut, equivalent to add(namespace, key, value, null)
     *
     * @param namespace The Namespace use to store the cache entry
     * @param key key of the new entry, key must be Serializable
     * @param value value for the new entry, value must be Serializable
     */
    private void add(String namespace, Object key, Object value) {
        add(namespace, key, value, null);
    }

    /**
     * Get content of cache for key, null if unset
     *
     * @param namespace the namespace used to store the cache entry
     * @param key the key object used to store the cache entry
     * @return the value object previously stored, or null
     */
    private Object get(String namespace, Object key) {
        try {

            MemcacheService cache = getService(namespace);

            return cache.get(key);

        } catch (Exception ignore) {}


        return null;
    }


    /**
     * Invalid cache in namespace for key
     *
     * @param namespace the namespace used to store the cache entry
     * @param key the key object used to store the cache entry
     */
    private void clean(String namespace, Object key) {
        getService(namespace).delete(key);
    }

    /**
     * Add a list of alarmClock into cache
     *
     * @param key User of alarm clock
     * @param value The list of alarm clock for new entry in cache
     */
    public void addListAlarmClock(User key, Collection<AlarmClockBean> value) {
        add(NAMESPACE_ALARM_CLOCK, key, value);
    }

    /**
     * Add an alarmClock into cache
     *
     * @param key Id of alarm clock
     * @param value Alarm clock for the new entry in cache
     */
    public void addAlarmClock(Long key, AlarmClockBean value) {
        add(NAMESPACE_ALARM_CLOCK, key, value);
    }

    /**
     * Get a list of alarmClock into cache
     *
     * @param key User of alarm clock
     * @return the list of {@link org.myLazyClock.services.bean.AlarmClockBean} previously stored, or null if not stored
     */
    public Collection<AlarmClockBean> getListAlarmClock(User key) {
        try {
            return (Collection<AlarmClockBean>) get(NAMESPACE_ALARM_CLOCK, key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Get an alarmClock into cache
     *
     * @param key Id of alarm clock
     * @return {@link org.myLazyClock.services.bean.AlarmClockBean} previously stored, or null if not stored
     */
    public AlarmClockBean getAlarmClock(Long key) {
        try {
            return (AlarmClockBean) get(NAMESPACE_ALARM_CLOCK, key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Invalid cache for alarmClock
     *
     * @param user user of alarmClock
     * @param alarmClockId id of alarmClock
     */
    public void cleanAlarmClock(User user, Long alarmClockId) {
        clean(NAMESPACE_ALARM_CLOCK, user);
        clean(NAMESPACE_ALARM_CLOCK, alarmClockId);
    }

    /**
     * Invalid cache for alarmClock and its calendar
     *
     * @param user user of alarmClock
     * @param alarmClockId id of alarmClock
     */
    public void cleanAllAlarmClock(User user, Long alarmClockId) {
        cleanAlarmClock(user, alarmClockId);
        clean(NAMESPACE_CALENDAR, forgeCalendarKey(user, alarmClockId));
    }

    /**
     * Forge an unique key with user and alarmClockId
     *
     * @param user user of alarm clock
     * @param alarmClockId id of alarm clock
     * @return the key used in memcache store
     */
    private String forgeCalendarKey(User user, Long alarmClockId) {
        return user.toString() + alarmClockId.toString();
    }

    /**
     * Get the list of {@link org.myLazyClock.services.bean.CalendarBean} in an alarm clock previously store in memcache
     *
     * @param user user of calendar
     * @param alarmClockId id of alarm clock
     * @return list of {@link org.myLazyClock.services.bean.CalendarBean} previously stored, null if not stored
     */
    public Collection<CalendarBean> getListCalendar(User user, Long alarmClockId) {
        try {
            return (Collection<CalendarBean>) get(NAMESPACE_CALENDAR, forgeCalendarKey(user, alarmClockId));
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Get a {@link org.myLazyClock.services.bean.CalendarBean} from cache
     *
     * @param user User of calendar
     * @param alarmClockId Alarm clock Id of calendar
     * @param calendarId Calendar Id
     * @return {@link org.myLazyClock.services.bean.CalendarBean} previously stored, null if not stored
     */
    public CalendarBean getCalendar(User user, Long alarmClockId, Long calendarId) {
        Collection<CalendarBean> listCalendar = getListCalendar(user, alarmClockId);
        if (listCalendar != null) {
            for (CalendarBean calendar : listCalendar) {
                if (calendarId.equals(calendar.getId())) {
                    return calendar;
                }
            }
        }
        return null;
    }

    /**
     * Add a list of {@link org.myLazyClock.services.bean.CalendarBean} in memcache store
     *
     * @param user user of calendar
     * @param alarmClockId id of alarm clock
     * @param listCalendar the list of calendar will be store in memcache
     */
    public void addListCalendar(User user, Long alarmClockId, Collection<CalendarBean> listCalendar) {
        add(NAMESPACE_CALENDAR, forgeCalendarKey(user, alarmClockId), listCalendar);
    }

    /**
     * Invalid cache for calendar
     *
     * @param user user of calendar
     * @param alarmClockId id of alarmClock
     */
    public void cleanCalendar(User user, Long alarmClockId) {
        clean(NAMESPACE_CALENDAR, forgeCalendarKey(user, alarmClockId));
    }

    /**
     * Get the user validity previously stored un memcache
     *
     * @param user user checked validity
     * @return the validity of user previously stored, null if not stored
     */
    public MyLazyClockUserValid getUserValidity(User user) {
        try {
            return (MyLazyClockUserValid) get(NAMESPACE_USER_VALIDITY, user);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Add user validity in memcache store,
     *    this store will expire in 6 hours
     * @param user user checked validity
     * @param isValid result of check will be store
     */
    public void addUserValidity(User user, MyLazyClockUserValid isValid) {
        add(NAMESPACE_USER_VALIDITY, user, isValid, Expiration.byDeltaSeconds(21600)); // 6J
    }

    /**
     * Invalid cache of user validity
     *
     * @param user user checked validity
     */
    public void cleanUserValidity(User user) {
        clean(NAMESPACE_USER_VALIDITY, user);
    }

    /**
     * Get the list of UFR previously stored in memcache
     *
     * @return list of UFR, null id not stored
     */
    public Collection<EdtData> getListUfr() {
        try {
            return (Collection<EdtData>) get(NAMESPACE_SMART_EDT, "listUFR");
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Add the list of UFR in memcache store,
     *      this store will expire in 1 week
     *
     * @param listUfr list of UFR will be store
     */
    public void addListUFR(Collection<EdtData> listUfr) {
       add(NAMESPACE_SMART_EDT, "listUFR", listUfr, Expiration.byDeltaSeconds(604800)); // 1 week
    }

    /**
     * Get a list of group in the UFR previously stored in memcache
     *
     * @param ufr id of ufr
     * @return list of group in the UFR previously stored, null if not stored
     */
    public Collection<EdtData> getListGroupsUfr(String ufr) {
        try {
            return (Collection<EdtData>) get(NAMESPACE_SMART_EDT, ufr);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Add the list of group on an UFR in memcache store,
     *      this store will expire in 1 day
     *
     * @param ufr id of ufr
     * @param listGroup list of group in the ufr will be store
     */
    public void addGroupsUfr(String ufr, Collection<EdtData> listGroup) {
        add(NAMESPACE_SMART_EDT, ufr, listGroup, Expiration.byDeltaSeconds(86400)); // 24h
    }

}
