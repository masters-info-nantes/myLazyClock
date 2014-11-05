package org.myLazyClock.travelApi;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public interface TravelStrategy {

    /**
     * The identifier is used to get a specific strategy in {@link TravelFactory}
     *
     * The identifier must be unique between all implementation of {@link TravelStrategy}
     *
     * @return Identifier of the strategy
     */
    public Integer getId();

    /**
     * The name of the TravelStrategy can be use by user
     * who want choice the strategy of travel
     *
     * @return Name of the TravelStrategy
     */
    public String getName();
}
