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

import java.util.Calendar;

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
     * With specifique url get source on one day and return the first event of calandar of the day
     *
     * @param url url of source
     * @param day day in which search event
     * @return CalendarEvent the first event find
     * @throws EventNotFoundException if not event found in specific day
     */
    public CalendarEvent getFirstEvent(String url, Calendar day) throws EventNotFoundException;
}
