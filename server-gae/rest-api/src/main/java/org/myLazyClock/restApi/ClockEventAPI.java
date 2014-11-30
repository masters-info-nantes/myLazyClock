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
import org.myLazyClock.calendarApi.CalendarEvent;
import org.myLazyClock.calendarApi.CalendarStrategy;
import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.model.AlarmClockEvent;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.model.repository.CalendarRepository;
import org.myLazyClock.services.CalendarModulesService;
import org.myLazyClock.services.ClockEventService;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class ClockEventAPI {

    @ApiMethod(name = "clockevent.list", httpMethod = ApiMethod.HttpMethod.GET, path="clockevent")
    public Collection<AlarmClockEvent> list(@Named("alarmClockId") String alarmClockId, User user) throws ForbiddenException, NotFoundException {
        AlarmClock alarm = AlarmClockRepository.getInstance().findOne(alarmClockId);
        return ClockEventService.getInstance().listEventForWeek(alarm);
    }
}
