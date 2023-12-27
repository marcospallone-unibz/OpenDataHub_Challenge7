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
import java.util.*;

public class SecondAPI implements OpenDataHubApiClient {
    OpenDataHubApiConfig config;
    JSONObject objectFromAPI;
    JSONArray splittedArray;

    public SecondAPI(OpenDataHubApiConfig config) throws Exception {
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
        this.objectFromAPI = jsonObject;
    }

    /*@Override
    public Map<List<Object>, JSONObject> splitPath() {
        String[] steps = this.config.pathToItem.split(">");
        for (String s : steps) {
            this.splittedArray = (JSONArray) objectFromAPI.get(s);
        }
        List<Mapping> mappings = this.config.mapping;
        List<String> keyPaths = new ArrayList<>();
        for (Mapping mapping : mappings) {
            keyPaths.add(mapping.getKeyPath());
        }
        return mapData(objectFromAPI, keyPaths);


    }

    private Map<List<Object>, JSONObject> mapData(JSONObject jsonData, List<String> keyPaths) {
        Map<List<Object>, JSONObject> mappedData = new HashMap<>();
        String[] pathStepToItem = this.config.pathToItem.split(">");
        Object items = objectFromAPI;
        for (String nextStep : pathStepToItem) {
            items = goIntoJSON(items, nextStep);
        }
        JSONArray itemArray = (JSONArray) items;
        for (Object o : itemArray) {
            JSONObject item = (JSONObject) o;
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
        JSONObject currentObject = jsonObject;
        String lastKey = keys[keys.length - 1];
        for (int i = 0; i < keys.length; i++) {
            if (currentObject.containsKey(keys[i])) {
                Object value = currentObject.get(keys[i]);
                if (value instanceof JSONObject) {
                    currentObject = (JSONObject) value;
                } else if (value instanceof JSONArray && Objects.equals(keys[i + 1], lastKey)) {
                    return handleJsonArrayValue((JSONArray) value, lastKey);
                } else {
                    return value;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    private static Object handleJsonArrayValue(JSONArray jsonArray, String lastKey) {
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject jsonObj) {
                if (jsonObj.containsKey(lastKey)) {
                    return jsonObj.get(lastKey);
                }
            }
        }
        return null;
    }

    public Object goIntoJSON(Object obj, String nextStep) {
        Object returnValue = null;
        if (obj instanceof JSONArray jsonArray) {
            for (Object objInJsonArray : jsonArray) {
                if (objInJsonArray instanceof JSONObject jsonObj) {
                    if (jsonObj.containsKey(nextStep)) {
                        return jsonObj.get(nextStep);
                    }
                }
            }
        } else if (obj instanceof JSONObject JSONObject) {
            returnValue = JSONObject.get(nextStep);
        }
        return returnValue;
    }*/
}


