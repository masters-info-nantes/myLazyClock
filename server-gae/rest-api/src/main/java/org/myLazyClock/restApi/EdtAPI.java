/*
 * myLazyClock
 *
 * Copyright (C) 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.myLazyClock.restApi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import org.myLazyClock.services.EdtService;
import org.myLazyClock.services.bean.EdtData;

import java.io.IOException;
import java.util.Collection;

/**
 * Created on 22/10/14.
 *
 * @author dralagen, Maxime
 */
@Api(
    name = Constants.NAME,
    version = Constants.VERSION,
    clientIds = { Constants.WEB_CLIENT_ID},
    scopes = {Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ}
)
public class EdtAPI {

    private MemcacheService getMemcacheService() {
        MemcacheService cache = MemcacheServiceFactory.getMemcacheService("smartEDT");
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Constants.MEMCACHE_LEVEL_ERROR_HANDLERS));
        return cache;
    }

    @ApiMethod(name = "edt.groups.list", httpMethod = ApiMethod.HttpMethod.GET, path="edt/groups/")
    public Collection<EdtData> getGroupsList(@Named("ufr") String ufr) throws IOException {
        Collection<EdtData> listGroup;
        MemcacheService cache = getMemcacheService();

        try {
            listGroup = (Collection<EdtData>) cache.get(ufr);
            if (listGroup != null) {
                return listGroup;
            }
        } catch (Exception ignore) { }

        listGroup = EdtService.getInstance().getGroupsList(ufr);

        try {
            cache.put(ufr, listGroup, Expiration.byDeltaSeconds(86400)); // 24H
        } catch (Exception ignore) {}

        return listGroup;

    }

    @ApiMethod(name = "edt.ufr.list", httpMethod = ApiMethod.HttpMethod.GET, path="edt/ufr/")
    public Collection<EdtData> getUFRList() throws IOException {

        MemcacheService cache = getMemcacheService();
        Collection<EdtData> listUfr;

        try {
            listUfr = (Collection<EdtData>) cache.get("listUFR");
            if (listUfr != null) {
                return listUfr;
            }
        } catch (Exception ignore) { }

        listUfr = EdtService.getInstance().getUFRList();

        try {
            cache.put("listUFR", listUfr, Expiration.byDeltaSeconds(604800)); // 1 week
        } catch (Exception ignore) {}

        return listUfr;

    }

}
