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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.myLazyClock.model.model.EdtData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private static String  url = "http://smart-edt.fr/android/data.php";

    private static JsonElement getJson(List<NameValuePair> urlParameters) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        JsonParser jp = new JsonParser();
        return jp.parse(rd);
    }

    @ApiMethod(name = "edt.groups.list", httpMethod = ApiMethod.HttpMethod.GET, path="edt/groups/")
    public Collection<EdtData> getGroupsList(@Named("ufr") String ufr) throws IOException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("getComp", ufr));

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
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("getSchool", "1"));

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
