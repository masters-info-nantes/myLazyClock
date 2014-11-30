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

package org.myLazyClock.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.myLazyClock.calendarApi.JsonConverter;
import org.myLazyClock.model.bean.EdtData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created on 30/11/14.
 *
 * @author dralagen
 */
public class EdtService {

    private static EdtService service = null;

    private EdtService() {}

    public static synchronized EdtService getInstance() {
        if (null == service) {
            service = new EdtService();
        }
        return service;
    }

    public Collection<EdtData> getGroupsList(String ufr) throws IOException {

        HashMap<String, String> urlParameters = new HashMap<String, String>();
        urlParameters.put("getComp", ufr);

        JsonObject root = JsonConverter.getJson(urlParameters).getAsJsonArray().get(0).getAsJsonObject();

        JsonArray array = root.get("groups").getAsJsonArray();

        ArrayList<EdtData> edts = new ArrayList<EdtData>();
        for(int i = 0; i<array.size(); i++) {
            JsonObject a = array.get(i).getAsJsonObject();
            EdtData edtData = new EdtData();
            edtData.setId(a.get("id").getAsString());
            edtData.setName(a.get("name").getAsString());
            edts.add(edtData);
        }
        return edts;
    }

    public Collection<EdtData> getUFRList() throws IOException {
        HashMap<String, String> urlParameters = new HashMap<String, String>();
        urlParameters.put("getSchool", "1");

        JsonObject root = JsonConverter.getJson(urlParameters).getAsJsonArray().get(0).getAsJsonObject();

        JsonArray array = root.get("comps").getAsJsonArray();

        ArrayList<EdtData> edts = new ArrayList<EdtData>();
        for(int i = 0; i<array.size(); i++) {
            JsonObject a = array.get(i).getAsJsonObject();
            EdtData edtData = new EdtData();
            edtData.setId(a.get("id").getAsString());
            edtData.setName(a.get("name").getAsString());
            edts.add(edtData);
        }
        return edts;
    }
}
