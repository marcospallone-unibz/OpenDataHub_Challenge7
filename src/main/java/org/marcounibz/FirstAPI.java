package org.marcounibz;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.marcounibz.interfaces.OpenDataHubApiClient;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FirstAPI implements OpenDataHubApiClient {

    OpenDataHubApiConfig config;
    JSONObject objectFromAPI;

    public FirstAPI(OpenDataHubApiConfig config) throws Exception {
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
}

