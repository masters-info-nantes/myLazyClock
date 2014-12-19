package org.myLazyClock.travelApi;

/**
 * Created on 03/12/14.
 *
 * @author david
 */
public class TravelDuration {

    /**
     * Correspond to duration of travel in second
     */
    private long time;

    /**
     * Create new travel duration
     *
     * @param time duration of travel in second
     */
    public TravelDuration(long time){

        this.time = time;
    }

    /**
     * Get time in second of duration of travel
     *
     * @return duration of travel in second
     */
    public long getTime() {

        return time;
    }

    /**
     * Set time in second of duration of travel
     *
     * @param time duration of travel in second
     */
    public void setTime(long time) {
        this.time = time;
    }
}
