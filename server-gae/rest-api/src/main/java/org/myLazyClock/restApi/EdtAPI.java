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
import com.google.gson.*;
import org.myLazyClock.model.model.EdtData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Maxime on 22/10/14.
 */

@Api(
    name = Constants.NAME,
    version = Constants.VERSION,
    clientIds = { Constants.WEB_CLIENT_ID, Constants.WEB_CLIENT_ID_DEV,  Constants.WEB_CLIENT_ID_DEV_WEB},
    scopes = {Constants.SCOPE_EMAIL, Constants.SCOPE_CALENDAR_READ}
)
public class EdtAPI {

    private static String  urlSmartEdt = "http://smart-edt.fr/android/data.php";

    private static JsonElement getJson(Map<String, String> params) throws IOException {
        URL url = new URL(urlSmartEdt);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        conn.connect();

        JsonParser jp = new JsonParser();
        return jp.parse(new InputStreamReader((InputStream) conn.getContent()));
    }

    private static String getQuery(Map<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> pair : params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @ApiMethod(name = "edt.groups.list", httpMethod = ApiMethod.HttpMethod.GET, path="edt/groups/")
    public Collection<EdtData> getGroupsList(@Named("ufr") String ufr) throws IOException {
        HashMap<String, String> urlParameters = new HashMap<>();
        urlParameters.put("getComp", ufr);

        JsonObject root = getJson(urlParameters).getAsJsonArray().get(0).getAsJsonObject();

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

    @ApiMethod(name = "edt.ufr.list", httpMethod = ApiMethod.HttpMethod.GET, path="edt/ufr/")
    public Collection<EdtData> getUFRList() throws IOException {
        HashMap<String, String> urlParameters = new HashMap<>();
        urlParameters.put("getSchool", "1");

        JsonObject root = getJson(urlParameters).getAsJsonArray().get(0).getAsJsonObject();

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
