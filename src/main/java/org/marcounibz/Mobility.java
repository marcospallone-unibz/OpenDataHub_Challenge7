package org.marcounibz;

import org.json.simple.JSONObject;
import org.marcounibz.interfaces.OpenDataHubApiClient;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.util.List;
import java.util.Map;

public class Mobility implements OpenDataHubApiClient {

    OpenDataHubApiConfig config;

    public Mobility(OpenDataHubApiConfig config){
        this.config = config;
    }

    @Override
    public void fetchDataFromApi() throws Exception {

    }

    @Override
    public JSONObject splitPath() {
        return null;
    }
}
