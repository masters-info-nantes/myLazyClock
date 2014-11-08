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
    scopes = { Constants.EMAIL_SCOPE }
)
public class AlarmClockAPI {

    @ApiMethod(name = "list", httpMethod = ApiMethod.HttpMethod.GET, path="list")
    public Collection<AlarmClock> getAllByUser(User user) {
        System.out.print(user.getEmail());
        return AlarmClockService.getInstance().findAll(user.getUserId());
    }

    @ApiMethod(name = "listAll", httpMethod = ApiMethod.HttpMethod.GET, path="listAll")
    public Collection<AlarmClock> getAll() {
        return AlarmClockService.getInstance().findAll();
    }

    @ApiMethod(name = "item", httpMethod = ApiMethod.HttpMethod.GET, path="item")
    public AlarmClock item(@Named("alarmClockId") String alarmClockId, User user) {
        return AlarmClockService.getInstance().item(alarmClockId, user.getUserId());
    }

    @ApiMethod(name = "generate", httpMethod = ApiMethod.HttpMethod.GET, path="generate")
    public AlarmClock generate() {
        return AlarmClockService.getInstance().generate();
    }

    @ApiMethod(name = "link", httpMethod = ApiMethod.HttpMethod.POST, path="link")
    public AlarmClock link(@Named("alarmClockId") String alarmClockId, User user) {
        return AlarmClockService.getInstance().link(alarmClockId, user.getUserId());
    }

}
