package org.myLazyClock.services;

import org.myLazyClock.travelApi.TravelFactory;
import org.myLazyClock.travelApi.TravelStrategy;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class TravelModulesServices {

    public String listModule() {
        String result = "";

        TravelFactory factory = TravelFactory.getInstance();

        for ( TravelStrategy strategy : factory) {
            result += strategy.getName() + " : " + strategy.getId() + "\n";
        }

        return result;
    }
}
