package org.marcounibz;

import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.util.List;
import java.util.Map;

public interface OpenDataHubApiClient {
    public List<Map<String, Object>> fetchDataFromApi(OpenDataHubApiConfig config) throws Exception;
}
