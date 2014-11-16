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
