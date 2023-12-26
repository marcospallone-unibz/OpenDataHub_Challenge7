package org.marcounibz.configurationHelper;


import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.io.FileReader;
import java.io.IOException;

public class ConfiguratorReader {
    JSONParser parser = new JSONParser();
    Gson gson = new Gson();
    OpenDataHubApiConfig firstConfig;
    OpenDataHubApiConfig secondConfig;


    public void readDataFromConfigurationFile() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/config.json"));
        JSONArray apiClients = (JSONArray) jsonObject.get("apiClients");

        JSONObject api1 = (JSONObject) apiClients.get(0);
        JSONObject api2 = (JSONObject) apiClients.get(1);

        firstConfig = gson.fromJson(api1.toString(), OpenDataHubApiConfig.class);
        secondConfig = gson.fromJson(api2.toString(), OpenDataHubApiConfig.class);

    }

    public OpenDataHubApiConfig getFirstConfig() {
        OpenDataHubApiConfig copyOfMobilityConfig = this.firstConfig;
        return copyOfMobilityConfig;
    }

    public OpenDataHubApiConfig getTourismConfigConfig() {
        OpenDataHubApiConfig copyOfTourismConfig = this.secondConfig;
        return copyOfTourismConfig;
    }

}
