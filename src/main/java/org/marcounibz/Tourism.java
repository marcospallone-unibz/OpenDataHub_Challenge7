package org.marcounibz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tourism {

    private List<Map<String, Object>> itemList = new ArrayList<>();

    public void getData() throws IOException, ParseException {
        URL url = new URL("https://tourism.opendatahub.com/v1/WeatherHistory?pagenumber=1&removenullvalues=false");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream inputStream = con.getInputStream();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));
        JSONArray itemsJsonArray = (JSONArray) jsonObject.get("Items");

        List<Map<String, Object>> itemList = new ArrayList<>();
        for (Object o : itemsJsonArray) {
            JSONObject itemJson = (JSONObject) o;

            Map<String, Object> itemMap = new HashMap<>();
            for (Object key : itemJson.keySet()) {
                Object value = itemJson.get(key);
                itemMap.put((String) key, value);
            }

            itemList.add(itemMap);
        }
        System.out.println(itemList);
        con.disconnect();
    }
}