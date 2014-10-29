package org.myLazyClock.travelApi;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class TravelManualStrategy implements TravelStrategy  {

    public static final int ID = 1;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Travel Manual";
    }
}
