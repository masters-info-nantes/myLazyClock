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

package org.myLazyClock.travelApi.exception;

/**
 * Created on 09/12/14.
 *
 * @author dralagen
 */
public class TravelNotFoundException extends Exception {

    public TravelNotFoundException() {
        super();
    }

    public TravelNotFoundException(String msg) {
        super(msg);
    }

    public TravelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TravelNotFoundException(Throwable cause) {
        super(cause);
    }
}
