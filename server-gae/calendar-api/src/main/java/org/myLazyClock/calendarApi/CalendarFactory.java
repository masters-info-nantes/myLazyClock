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

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarFactory implements Iterable<CalendarStrategy> {
    private static CalendarFactory ourInstance = new CalendarFactory();

    public static CalendarFactory getInstance() {
        return ourInstance;
    }

    /**
     * List all module who implement {@link CalendarStrategy}
     */
    private Map<CalendarId, CalendarStrategy> allModules;

    /**
     * Search all module who implement {@link CalendarStrategy}
     * and put in allModule
     *
     */
    private CalendarFactory() {
        allModules = new HashMap<>();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris")); // For Calendar work's on Google servers

        Reflections reflections = new Reflections("org.myLazyClock.calendarApi");
        Set<Class<? extends CalendarStrategy>> modules = reflections.getSubTypesOf(CalendarStrategy.class);

        for (Class<? extends CalendarStrategy> aModule : modules) {
            try {
                CalendarStrategy instance = aModule.newInstance();

                CalendarId id = instance.getId();

                if (allModules.containsKey(id)) {
                    throw new IllegalStateException(
                        "Strategy [" +
                        instance.getName() + "] with id " + id +
                        ", can not be used since the id is already used " +
                        "by strategy [" + allModules.get(id).getName() + "] ");
                }
                allModules.put(id, instance);

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Select the {@link CalendarStrategy} with id
     *
     * @param id Identifier of the {@link CalendarStrategy}
     * @return the {@link CalendarStrategy} if find,
     * @throws NullPointerException
     */
    public CalendarStrategy get(CalendarId id) throws NullPointerException {
        CalendarStrategy strategy = allModules.get(id);
        if (strategy == null) {
            throw new NullPointerException();
        }
        return strategy;
    }

    @Override
    public Iterator<CalendarStrategy> iterator() {
        return allModules.values().iterator();
    }
}
