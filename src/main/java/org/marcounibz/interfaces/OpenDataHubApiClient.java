package org.marcounibz.interfaces;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public interface OpenDataHubApiClient {
    void fetchDataFromApi() throws Exception;
    JSONObject splitPath();
}
