package org.myLazyClock.restApi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import org.myLazyClock.model.model.AlarmClock;
import org.myLazyClock.services.AlarmClockService;

import java.util.Collection;

/**
 * Created by Maxime on 22/10/14.
 */

@Api(
    name = Constants.NAME,
    version = Constants.VERSION,
    clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
    scopes = {Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ}
)
public class AlarmClockAPI {

    @ApiMethod(name = "alarmClock.list", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/list")
    public Collection<AlarmClock> getAllByUser(User user) {
        return AlarmClockService.getInstance().findAll(user.getUserId());
    }

    @ApiMethod(name = "alarmClock.listAll", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/listAll")
    public Collection<AlarmClock> getAll() {
        return AlarmClockService.getInstance().findAll();
    }

    @ApiMethod(name = "alarmClock.item", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/item")
    public AlarmClock item(@Named("alarmClockId") String alarmClockId) {
        return AlarmClockService.getInstance().item(alarmClockId);
    }

    @ApiMethod(name = "alarmClock.generate", httpMethod = ApiMethod.HttpMethod.GET, path="alarmClock/generate")
    public AlarmClock generate() {
        return AlarmClockService.getInstance().generate();
    }

    @ApiMethod(name = "alarmClock.link", httpMethod = ApiMethod.HttpMethod.POST, path="alarmClock/link")
    public AlarmClock link(AlarmClock alarmClock, User user) {
        return AlarmClockService.getInstance().link(alarmClock, user.getUserId());
    }

}
