package org.marcounibz;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.marcounibz.interfaces.OpenDataHubApiClient;
import org.marcounibz.mapping.Mapping;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mobility implements OpenDataHubApiClient {

    OpenDataHubApiConfig config;
    JSONObject objectFromAPI;
    JSONArray splittedArray;
    List<String[]> keyPaths = new ArrayList<>();
    List<Object> keys = new ArrayList<>();
    Map<List<Object>, JSONObject> mappedData;

    public Mobility(OpenDataHubApiConfig config) throws Exception {
        this.config = config;
        fetchDataFromApi();
    }

    @Override
    public void fetchDataFromApi() throws Exception {
        URL url = new URL(this.config.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        connection.disconnect();
        this.objectFromAPI=jsonObject;
    }

    @Override
    public Map<List<Object>, JSONObject> splitPath() {
        String[] steps = this.config.pathToItem.split(">");
        for(String s:steps){
            this.splittedArray = (JSONArray) objectFromAPI.get(s);
        }
        List<Mapping> mappings = this.config.mapping;
        List<String> keyPaths = new ArrayList<>();
        for(int i = 0; i<mappings.size(); i++) {
            keyPaths.add(mappings.get(i).getKeyPath());
        }
        return mapData(objectFromAPI, keyPaths);

    }

    private  Map<List<Object>, JSONObject> mapData(JSONObject jsonData, List<String> keyPaths) {
        Map<List<Object>, JSONObject> mappedData = new HashMap<>();
        String[] pathStepToItem = this.config.pathToItem.split(">");
        Object items = objectFromAPI;
        for(String nextStep: pathStepToItem){
            items = goIntoJSON(items,nextStep);
        }
        JSONArray itemArray = (JSONArray) items;
        for (int i = 0; i < itemArray.size(); i++) {
            JSONObject item = (JSONObject) itemArray.get(i);
            List<Object> key = new ArrayList<>();

            for (String keyPath : keyPaths) {
                Object keyValue = getValueByPath(item, keyPath);
                key.add(keyValue);
            }

            mappedData.put(key, item);
        }

        return mappedData;
    }

    private static Object getValueByPath(JSONObject jsonObject, String path) {
        String[] keys = path.split(">");
        Object currentObject = jsonObject;
        String lastKey = keys[keys.length-1];
        for (int i = 0; i< keys.length; i++) {
                currentObject = goIntoJSON(currentObject, keys[i]);
                if(keys[i]==lastKey) return currentObject;
        }

        return null;
    }

    public static Object goIntoJSON(Object obj, String nextStep){
        Object returnValue = null;
        if(obj instanceof JSONArray ){
            JSONArray jsonArray = (JSONArray) obj;
            for (Object objInJsonArray : jsonArray) {
                if (objInJsonArray instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) objInJsonArray;
                    if (jsonObj.containsKey(nextStep)) {
                        Object foundValue = jsonObj.get(nextStep);
                        return foundValue;
                    }
                }
            }
        }else if(obj instanceof  JSONObject){
            JSONObject JSONObject = (JSONObject) obj;
            returnValue = JSONObject.get(nextStep);
        }
        return returnValue;
    }
}

