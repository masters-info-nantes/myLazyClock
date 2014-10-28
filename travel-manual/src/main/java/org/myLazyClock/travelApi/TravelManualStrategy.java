package org.myLazyClock.travelApi;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class TravelManualStrategy implements TravelStrategy  {

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public String getName() {
        return "Travel Manual";
    }
}
