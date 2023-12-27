package org.marcounibz;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.marcounibz.configurationHelper.ConfiguratorReader;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.io.IOException;
import java.util.*;

public class DataManager {
    FirstAPI firstAPI;
    SecondAPI secondAPI;
    OpenDataHubApiConfig firstConfig;
    OpenDataHubApiConfig secondConfig;
    JSONObject firstAPIObject;
    JSONObject secondAPIObject;
    boolean duplicatesFound = false;
    Map<String, Object> mergedMap = new HashMap<>();

    public DataManager() throws Exception {
        setConfiguration();
        this.firstAPI = new FirstAPI(this.firstConfig);
        this.secondAPI = new SecondAPI(this.secondConfig);
        this.firstAPIObject = this.firstAPI.objectFromAPI;
        this.secondAPIObject = this.secondAPI.objectFromAPI;
    }

    public void setConfiguration() throws IOException, ParseException {
        ConfiguratorReader configuratorReader = new ConfiguratorReader();
        configuratorReader.readDataFromConfigurationFile();
        this.firstConfig = configuratorReader.getFirstConfig();
        this.secondConfig = configuratorReader.getTourismConfigConfig();
    }

    public void checkDuplicates() {
        Object returnValue = new Object();
        Object firstAPIValues = this.firstAPIObject;
        Object secondAPIValues = this.secondAPIObject;
        String[] firstAPISteps = this.firstConfig.keyWhereCheckDuplicate.split(">");
        String[] secondAPISteps = this.secondConfig.keyWhereCheckDuplicate.split(">");
        List<Object> duplicatesValue = new ArrayList<>();

        for (String s : firstAPISteps) {
            firstAPIValues = goIntoJSON(firstAPIValues, s);
        }
        for (String s : secondAPISteps) {
            secondAPIValues = goIntoJSON(secondAPIValues, s);
        }
        if(firstAPIValues instanceof ArrayList<?> firstArray){
            if(secondAPIValues instanceof ArrayList<?> secondArray){
                for(Object obj1:firstArray){
                    for(Object obj2:secondArray){
                        if (obj1.equals(obj2)) {
                            this.duplicatesFound = true;
                            duplicatesValue.add(obj1);
                        }
                    }
                }
            }
        }
        getDuplicatesKeys(duplicatesValue);
    }

    public Set<String> getDuplicatesKeys(List<Object> duplicates){
        Set<String> keys = new HashSet<>();
        if(this.duplicatesFound){
            for(Object o:duplicates){
                for (Map.Entry<String, Object> entry : this.mergedMap.entrySet()) {
                    if (Objects.equals(o, entry.getValue())) {
                        keys.add(entry.getKey());
                    }
                }
            }
        }
        return keys;
    }

    public JSONObject prepareValueToReturn(){
        JSONObject merged = new JSONObject();
        assert this.firstAPIObject != null;
        assert this.secondAPIObject != null;
        Set<String> firstAPIKeys = this.firstAPIObject.keySet();
        Set<String> secondAPIKeys = this.secondAPIObject.keySet();
        firstAPIKeys.forEach((e) ->{
            merged.put(e, this.firstAPIObject.get(e));
        });

        secondAPIKeys.forEach((e) ->{
            merged.put(e, this.secondAPIObject.get(e));
        });
        return merged;
    }

    public Object goIntoJSON(Object obj, String nextStep){
        Object returnValue = null;
        List<Object> moreValue= new ArrayList<>();
        returnValue = goIntoAnnidate(obj, moreValue, nextStep, returnValue);
        return returnValue;
    }

    public Object goIntoAnnidate(Object obj, List<Object> moreValue, String nextStep, Object returnValue){
        if(obj instanceof ArrayList<?> arrayList){
            for (Object objInJsonArray : arrayList) {
                if (objInJsonArray instanceof JSONObject jsonObj) {
                    if (jsonObj.containsKey(nextStep)) {
                        moreValue.add(jsonObj.get(nextStep));
                        this.mergedMap.put(nextStep, jsonObj.get(nextStep));
                    }
                } else {
                    goIntoAnnidate(objInJsonArray, moreValue, nextStep, returnValue);
                }
            }
            return moreValue;
        }else if(obj instanceof JSONObject JSONObject){
            returnValue = JSONObject.get(nextStep);
            this.mergedMap.put(nextStep, JSONObject.get(nextStep));
        }
        return returnValue;
    }
}
