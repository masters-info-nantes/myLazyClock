package org.myLazyClock.restApi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import org.myLazyClock.calendarApi.exception.ForbiddenCalendarException;
import org.myLazyClock.services.ClockEventService;
import org.myLazyClock.services.bean.AlarmClockEvent;
import org.myLazyClock.services.exception.NotFoundMyLazyClockException;

import java.util.Collection;

/**
 * Created on 17/11/14.
 *
 * @author Maxime
 */
@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class ClockEventAPI {

    @ApiMethod(name = "clockevent.list", httpMethod = ApiMethod.HttpMethod.GET, path="clockevent")
    public Collection<AlarmClockEvent> list(@Named("alarmClockId") String alarmClockId) throws ForbiddenException, NotFoundException {
        try {
            return ClockEventService.getInstance().listEventForWeek(alarmClockId);
        } catch (ForbiddenCalendarException e) {
            throw new ForbiddenException(e);
        } catch (NotFoundMyLazyClockException e) {
            throw new NotFoundException(e);
        }
    }
}
