package org.myLazyClock.travelApi;

/**
 * Created on 03/12/14.
 *
 * @author david
 */
public class TravelDuration {
    private long TimeMin;

    public TravelDuration(int timeMin){

        this.TimeMin=timeMin;
    }

    public long getTimeMin() {

        return TimeMin;
    }

    public void setTimeMin(long timeMin) {
        TimeMin = timeMin;
    }
}
