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

public class Tourism implements OpenDataHubApiClient {
    OpenDataHubApiConfig config;
    JSONObject objectFromAPI;
    JSONArray splittedArray;
    List<String[]> keyPaths = new ArrayList<>();
    List<Object> keys = new ArrayList<>();
    Map<Object, JSONObject> objectMap;

    public Tourism(OpenDataHubApiConfig config){
        this.config = config;
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
    public JSONObject splitPath() {
        String[] steps = this.config.pathToItem.split(">");
        for(String s:steps){
            this.splittedArray = (JSONArray) objectFromAPI.get(s);
        }
        List<Mapping> mappings = this.config.mapping;
        List<String> keyPaths = new ArrayList<>();
        for(int i = 0; i<mappings.size(); i++) {
            keyPaths.add(mappings.get(i).getKeyPath());
        }
        Map<List<Object>, JSONObject> mappedData = mapData(objectFromAPI, keyPaths);

        return null;
    }

/*/*

    private static JSONArray getNestedArray(JSONObject jsonObject, String... keys) {
        for (String key : keys) {
            jsonObject = (JSONObject) jsonObject.get(key);
        }
        return (JSONArray) jsonObject.get(keys[keys.length - 1]);
    }*/

    private static Map<List<Object>, JSONObject> mapData(JSONObject jsonData, List<String> keyPaths) {
        Map<List<Object>, JSONObject> mappedData = new HashMap<>();
        //GENERALIZZARE ITEMS!
        JSONArray dataArray = (JSONArray) jsonData.get("Items");

        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject item = (JSONObject) dataArray.get(i);
            List<Object> key = new ArrayList<>();

            for (String keyPath : keyPaths) {
                Object keyValue = getValueByPath(item, keyPath);
                key.add(keyValue);
            }

            mappedData.put(key, item);
        }

        return mappedData;
    }

    // Funzione per ottenere il valore di una chiave specifica utilizzando un percorso
    private static Object getValueByPath(JSONObject jsonObject, String path) {
        String[] keys = path.split(">");
        JSONObject currentObject = jsonObject;
        String lastKey = keys[keys.length-1];
        for (int i = 0; i< keys.length; i++) {
            if (currentObject.containsKey(keys[i])) {
                Object value = currentObject.get(keys[i]);
                if (value instanceof JSONObject) {
                    currentObject = (JSONObject) value;
                }else if(value instanceof JSONArray && keys[i+1]==lastKey){
                    Object jsonArrayReturnValue = handleJsonArrayValue((JSONArray) value, lastKey);
                    return jsonArrayReturnValue;
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
            if (obj instanceof JSONObject) {
                JSONObject jsonObj = (JSONObject) obj;

                if (jsonObj.containsKey(lastKey)) {
                    Object foundValue = jsonObj.get(lastKey);
                    return foundValue;
                }
            }
        }
    return null;
    }
}
