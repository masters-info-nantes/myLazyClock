package org.myLazyClock.travelApi;

/**
 * Created on 03/12/14.
 *
 * @author david
 */
public class TravelDuration {
    private long time;

    public TravelDuration(int time){

        this.time = time;
    }

    public long getTime() {

        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
