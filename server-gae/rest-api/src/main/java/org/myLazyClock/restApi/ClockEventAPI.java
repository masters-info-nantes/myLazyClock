package org.myLazyClock.restApi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.model.bean.AlarmClockEvent;
import org.myLazyClock.model.repository.AlarmClockRepository;
import org.myLazyClock.services.ClockEventService;

import java.util.*;

/**
 * Created on 17/11/14.
 *
 * @author Maxime
 */
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
