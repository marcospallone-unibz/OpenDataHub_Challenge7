package org.marcounibz;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.marcounibz.configurationHelper.ConfiguratorReader;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    FirstAPI firstAPI;
    SecondAPI secondAPI;
    OpenDataHubApiConfig firstConfig;
    OpenDataHubApiConfig secondConfig;
    JSONObject firstAPIObject;
    JSONObject secondAPIObject;

    public DataManager() throws Exception {
        setConfiguration();
        this.firstAPI = new FirstAPI(this.firstConfig);
        this.secondAPI = new SecondAPI(this.secondConfig);
        /*this.firstAPIObject = this.mobility.splitPath();
        this.secondAPIObject = this.tourism.splitPath();*/
        this.firstAPIObject = this.firstAPI.objectFromAPI;
        this.secondAPIObject = this.secondAPI.objectFromAPI;
    }

    public void setConfiguration() throws IOException, ParseException {
        ConfiguratorReader configuratorReader = new ConfiguratorReader();
        configuratorReader.readDataFromConfigurationFile();
        this.firstConfig = configuratorReader.getFirstConfig();
        this.secondConfig = configuratorReader.getTourismConfigConfig();
    }

    public Object checkDuplicates() {
        Object firstAPIValues = this.firstAPIObject;
        Object secondAPIValues = this.secondAPIObject;
        String[] firstAPISteps = this.firstConfig.keyWhereCheckDuplicate.split(">");
        String[] secondAPISteps = this.secondConfig.keyWhereCheckDuplicate.split(">");
        boolean duplicatesFound = false;
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
                            duplicatesFound = true;
                            break;
                        }
                    }
                }
            }
        }

        //LAVORARE SUI DUPLICATI TROVATI

        return null;    //CAMBIARE RETURN DOPO AVER LAVORATO SUI DUPLICATI

        /*Map<List<Object>, JSONObject> resultMap = new HashMap<>();
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
        return resultMap;*/

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
                    }
                } else {
                    goIntoAnnidate(objInJsonArray, moreValue, nextStep, returnValue);
                }
            }
            return moreValue;
        }else if(obj instanceof JSONObject JSONObject){
            returnValue = JSONObject.get(nextStep);
        }
        return returnValue;
    }
}
