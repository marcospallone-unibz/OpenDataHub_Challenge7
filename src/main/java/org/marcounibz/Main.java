package org.marcounibz;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.marcounibz.configurationHelper.ConfiguratorReader;
import org.marcounibz.mapping.Mapping;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static OpenDataHubApiConfig firstConfig;
    static OpenDataHubApiConfig secondConfig;

    public static void setConfiguration() throws IOException, ParseException {
        ConfiguratorReader configuratorReader = new ConfiguratorReader();
        configuratorReader.readDataFromConfigurationFile();
        firstConfig = configuratorReader.getFirstConfig();
        secondConfig = configuratorReader.getSecondConfig();
    }


    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        setConfiguration();
        List<Object> returnList = new ArrayList<>();
        int iteratingOnMappings = 0;
        for(Mapping m1: firstConfig.mapping) {
            for (Mapping m2 : secondConfig.mapping) {
                Object objectToAdd = dataManager.checkDuplicates(m1.getKeyPath(), m2.getKeyPath(), iteratingOnMappings);
                if(objectToAdd instanceof ArrayList jsonArray){
                    if(!jsonArray.isEmpty()){
                        returnList.add(objectToAdd);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("NODUPLICATESFOUNDWITH", m1.getKeyPath()+" -- "+m2.getKeyPath());
                        returnList.add(jsonObject);
                    }
                }
            }
            iteratingOnMappings++;
        }
        System.out.println(returnList);
    }
}