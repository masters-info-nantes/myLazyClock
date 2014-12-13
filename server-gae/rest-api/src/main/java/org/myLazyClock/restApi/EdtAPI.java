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
import org.myLazyClock.services.EdtService;
import org.myLazyClock.services.MyLazyClockMemcacheService;
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

    @ApiMethod(name = "edt.groups.list", httpMethod = ApiMethod.HttpMethod.GET, path="edt/groups/")
    public Collection<EdtData> getGroupsList(@Named("ufr") String ufr) throws IOException {

        Collection<EdtData> listGroup = MyLazyClockMemcacheService.getInstance().getListGroupsUfr(ufr);

        if (listGroup != null) {
            return listGroup;
        }

        listGroup = EdtService.getInstance().getGroupsList(ufr);

        MyLazyClockMemcacheService.getInstance().addGroupsUfr(ufr, listGroup);

        return listGroup;

    }

    @ApiMethod(name = "edt.ufr.list", httpMethod = ApiMethod.HttpMethod.GET, path="edt/ufr/")
    public Collection<EdtData> getUFRList() throws IOException {

        Collection<EdtData> listUfr = MyLazyClockMemcacheService.getInstance().getListUfr();

        if (listUfr != null) {
            return listUfr;
        }

        listUfr = EdtService.getInstance().getUFRList();

        MyLazyClockMemcacheService.getInstance().addListUFR(listUfr);

        return listUfr;

    }

}
