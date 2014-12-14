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

import org.myLazyClock.calendarApi.exception.EventNotFoundException;
import org.myLazyClock.calendarApi.exception.ForbiddenCalendarException;

import java.util.Map;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public interface CalendarStrategy {

    /**
     * The identifier is used to get a specific strategy in {@link CalendarStrategy}
     *
     * The identifier must be unique between all implementation of {@link CalendarStrategy}
     *
     * @return Identifier of the strategy
     */
    public Integer getId();

    /**
     * The name of the {@link CalendarStrategy} can be use by user
     * who want choice the strategy of travel
     *
     * @return Name of the {@link CalendarStrategy}
     */
    public String getName();
    
    /**
     * For specific module find the first Calendar between beginDate and endDay
     *
     * @param params Contain all parameter for module
     * @param beginDate Lower bound date in which search event
     * @param endDate Upper bound date in which search event
     * @return CalendarEvent the first event find
     * @throws org.myLazyClock.calendarApi.exception.EventNotFoundException if not event found in specific day
     */
    public CalendarEvent getFirstEvent(Map<String, String> params, java.util.Calendar beginDate, java.util.Calendar endDate) throws EventNotFoundException, ForbiddenCalendarException;
}
