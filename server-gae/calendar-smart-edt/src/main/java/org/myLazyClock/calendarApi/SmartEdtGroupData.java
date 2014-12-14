package org.myLazyClock.calendarApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremy on 09/12/14.
 */
public class SmartEdtGroupData {

    private int groupId;
    private Map<String, HashMap<Integer, String>> data;

    public SmartEdtGroupData(int groupId){
        this.groupId = groupId;
        this.data = new HashMap<String, HashMap<Integer, String>>();
    }

    public void reloadData() throws IOException {
        Map<String, String> urlParameters = new HashMap<String, String>();
        urlParameters.put("getGroup", String.valueOf(this.groupId));
        urlParameters.put("bdd0", "");

        JsonElement root = JsonConverter.getJson(urlParameters).getAsJsonArray().get(0);
        JsonArray bdd = root.getAsJsonObject().get("bdds").getAsJsonArray();

        /*
            Array content : teachers, rooms, group, course name, course category
            Mapped in bdd array from index 0 to 4 (5th is useless)
         */
        List<String> mapId = Arrays.asList("teachers", "rooms", "groups", "courseNames", "courseCategories");

        for(int i = 0; i < mapId.size(); i++){ // Constant lenght, don't use last bdd array case
            JsonElement bddElement = bdd.get(i);

            JsonArray elements = bddElement.getAsJsonObject().get("values").getAsJsonArray();
            HashMap<Integer, String> elementsMap = new HashMap<Integer, String>();

            for(JsonElement element: elements){
                int elementId = element.getAsJsonObject().get("id").getAsInt();
                String elementName = new String(
                        Charset.forName("ISO-8859-1")
                                .encode(
                                        element.getAsJsonObject()
                                                .get("name")
                                                .getAsString()
                                )
                                .array()
                );
                elementsMap.put(elementId, elementName);
            }
            this.data.put(mapId.get(i), elementsMap);
        }
    }

    public String getData(GroupDataType dataType, int dataId){
        Map<Integer, String> dataMap = this.data.get(dataType.getName());
        return dataMap.get(dataId);
    }
}
