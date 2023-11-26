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
        JSONObject testJSON;
        String[] steps = this.config.pathToItem.split(">");
        for(String s:steps){
            this.splittedArray = (JSONArray) objectFromAPI.get(s);
        }
        for(Mapping mapping:this.config.mapping){
            this.keyPaths.add(mapping.getKeyPath().split(">"));
        }
        /*for(int i=0; i<this.splittedArray.size(); i++){
            JSONObject myObj = (JSONObject) this.splittedArray.get(i);
            for(String[] array:this.keyPaths){
                JSONArray test2 = (JSONArray) myObj.get(array[0]);
                for(int j =1; j<array.length; j++){
                    test2 = (JSONObject) test2.get(array[j]);
                    if(j== array.length-1){
                        this.keys.add(test2);
                    }
                }
            }
        }*/
        return null;
    }
}