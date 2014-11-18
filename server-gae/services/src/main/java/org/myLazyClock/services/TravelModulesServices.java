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

import org.myLazyClock.travelApi.TravelFactory;
import org.myLazyClock.travelApi.TravelStrategy;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class TravelModulesServices {

    /**
     * List all module who implement {@link TravelStrategy}
     *
     * @return a Collection of all Travel modules
     */
    public Collection<TravelStrategy> listModule() {
        Collection<TravelStrategy> result = new ArrayList<TravelStrategy>();

        TravelFactory factory = TravelFactory.getInstance();

        for ( TravelStrategy strategy : factory) {
            result.add(strategy);
        }

        return result;
    }
}
