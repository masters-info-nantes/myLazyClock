package org.myLazyClock.restApi;

/**
 * Created on 16/11/14.
 *
 * @author dralagen
 */

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import org.myLazyClock.services.TravelModulesServices;
import org.myLazyClock.travelApi.TravelStrategy;

import java.util.Collection;

@Api(
        name = Constants.NAME,
        version = Constants.VERSION,
        clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
        scopes = { Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ }
)
public class TravelAPI {

    @ApiMethod(name = "travel.listAll", httpMethod = ApiMethod.HttpMethod.GET, path = "travel/list")
    public Collection<TravelStrategy> getAll() {
        TravelModulesServices services = new TravelModulesServices();

        return services.listModule();
    }
}
