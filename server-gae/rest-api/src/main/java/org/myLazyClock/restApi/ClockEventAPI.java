package org.myLazyClock.restApi;

/**
 * Created by Maxime on 17/11/14.
 */

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.AlarmClockEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class ClockEventAPI {

    @ApiMethod(name = "clockevent.list", httpMethod = ApiMethod.HttpMethod.GET, path="clockevent")
    public Collection<AlarmClockEvent> list(@Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException, NotFoundException {
        ArrayList<AlarmClockEvent> a = new ArrayList<AlarmClockEvent>();
        AlarmClockEvent e = new AlarmClockEvent();
        e.setAddress("15, rue kervegan, 44000 Nantes");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis()+30000);
        e.setBeginDate(cal.getTime());
        e.setTravelDuration(new Long(5));
        e.setName("Cours à la fac");
        AlarmClockEvent e1 = new AlarmClockEvent();
        e1.setAddress("15, rue kervegan, 44000 Nantes");
        cal.setTimeInMillis(cal.getTimeInMillis()+120000);
        e1.setBeginDate(cal.getTime());
        e1.setTravelDuration(new Long(5));
        e1.setName("Cours à la fac 2");
        a.add(e);
        a.add(e1);
        return a;
    }

}
