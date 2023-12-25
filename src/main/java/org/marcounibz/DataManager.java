package org.marcounibz;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.marcounibz.configurationHelper.ConfiguratorReader;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    Mobility mobility;
    Tourism tourism;
    OpenDataHubApiConfig mobilityConfig;
    OpenDataHubApiConfig tourismConfig;
    Map<List<Object>, JSONObject> mappedDataMobility;
    Map<List<Object>, JSONObject> mappedDataTourism;

    public DataManager() throws Exception {
        setConfiguration();
        this.mobility = new Mobility(this.mobilityConfig);
        this.tourism = new Tourism(this.tourismConfig);
        this.mappedDataMobility = this.mobility.splitPath();
        this.mappedDataTourism = this.tourism.splitPath();
    }

    public void setConfiguration() throws IOException, ParseException {
        ConfiguratorReader configuratorReader = new ConfiguratorReader();
        configuratorReader.readDataFromConfigurationFile();
        this.mobilityConfig = configuratorReader.getMobilityConfig();
        this.tourismConfig = configuratorReader.getTourismConfigConfig();
    }

    public Map<List<Object>, JSONObject> mergeData() {
        Map<List<Object>, JSONObject> resultMap = new HashMap<>();
        for (Object key : mappedDataMobility.keySet()) {
            if (mappedDataTourism.containsKey(key)) {
                System.out.println(key);
                System.out.println(mappedDataMobility.get(key));
                System.out.println(mappedDataTourism.get(key));
                mappedDataTourism.remove(key);
            }
        }
        resultMap.putAll(mappedDataTourism);
        resultMap.putAll(mappedDataMobility);
        return resultMap;
    }

}
