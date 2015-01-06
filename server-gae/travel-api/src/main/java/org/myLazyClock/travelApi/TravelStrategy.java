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

package org.myLazyClock.travelApi;

import org.myLazyClock.travelApi.exception.TravelNotFoundException;

import java.util.Date;
import java.util.Map;

/**
 * Created on 28/10/14.
 *
 * @author david, dralagen
 */
public interface TravelStrategy {

    /**
     * The identifier is used to get a specific strategy in {@link TravelFactory}
     *
     * The identifier must be unique between all implementation of {@link TravelStrategy}
     *
     * @return Identifier of the strategy
     */
    public TravelId getId();

    /**
     * The name of the TravelStrategy can be use by user
     * who want choice the strategy of travel
     *
     * @return Name of the TravelStrategy
     */
    public String getName();

    /**
     * Calculating duration of travel
     *
     * @param from  Address of depart
     * @param to    Address of arrival
     * @param dateArrival  Address date when you want arrive
     * @param params Some additional parameter for module specificity
     * @return the duration of travel {@link org.myLazyClock.travelApi.TravelDuration}
     * @throws TravelNotFoundException
     */
    public TravelDuration getDuration(String from, String to, Date dateArrival, Map<String,String> params) throws TravelNotFoundException;
}
