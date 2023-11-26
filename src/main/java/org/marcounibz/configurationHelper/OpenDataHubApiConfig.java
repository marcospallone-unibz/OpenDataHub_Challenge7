package org.marcounibz.configurationHelper;

import java.util.Map;

public class OpenDataHubApiConfig {
    private String name;
    private String url;

    private String key;
    private Map<String, String> mapping;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url= url;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }


}
