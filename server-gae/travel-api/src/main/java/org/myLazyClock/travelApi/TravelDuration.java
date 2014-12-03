package org.myLazyClock.travelApi;

/**
 * Created by david on 03/12/14.
 */
public class TravelDuration {
    private int TimeMin;

    public TravelDuration(int timeMin){

        this.TimeMin=timeMin;
    }

    public int getTimeMin() {

        return TimeMin;
    }

    public void setTimeMin(int timeMin) {
        TimeMin = timeMin;
    }
}
