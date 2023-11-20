package org.marcounibz;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Tourism {

    public JSONObject getData() throws IOException, ParseException {
        URL url = new URL("https://tourism.opendatahub.com/v1/WeatherHistory?pagenumber=1&removenullvalues=false");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream inputStream = con.getInputStream();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        /*JSONArray itemsJsonArray = (JSONArray) jsonObject.get("Items");*/

        /*List<Map<String, Object>> itemList = new ArrayList<>();
        for (Object o : itemsJsonArray) {
            JSONObject itemJson = (JSONObject) o;
            Map<String, Object> itemMap = new HashMap<>();
            for (Object key : itemJson.keySet()) {
                Object value = itemJson.get(key);
                itemMap.put((String) key, value);
            }
            itemList.add(itemMap);
        }
        System.out.println(itemList);*/

        con.disconnect();

        return jsonObject;
    }
}