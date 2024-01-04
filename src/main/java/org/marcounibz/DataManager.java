package org.marcounibz;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.marcounibz.configurationHelper.ConfiguratorReader;
import org.marcounibz.mapping.Mapping;
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
    List<String> replacementKeys = new ArrayList<>();
    List<Object> objectWhereDuplicates = new ArrayList<>();
    int indexWhereFoundReplacementKey;
    String keyPath1;
    String keyPath2;

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
        this.secondConfig = configuratorReader.getSecondConfig();
        this.replacementKeys = configuratorReader.getReplacementKeys();
    }

    public List<Object> checkDuplicates(String keyPath1, String keyPath2, int indexWhereFoundReplacementKey) {
        List<Object> returnList= new ArrayList<>();
        this.objectWhereDuplicates.clear();
        this.keyPath1=keyPath1;
        this.keyPath2=keyPath2;
        this.indexWhereFoundReplacementKey = indexWhereFoundReplacementKey;
        returnList = getDuplicates(returnList, keyPath1, keyPath2);
        if(duplicatesFound){
            return returnList;
        } else{
            List<Object> noDuplicatesFound = new ArrayList<>();
            JSONObject zeroDuplicatesJSONObj = new JSONObject();
            zeroDuplicatesJSONObj.put("NUMBER OF DUPLICATES FOUND", 0);
            noDuplicatesFound.add(zeroDuplicatesJSONObj);
            noDuplicatesFound.add(this.firstAPIObject);
            noDuplicatesFound.add(this.secondAPIObject);
            return noDuplicatesFound;
        }
    }

    private List<Object> getDuplicates(List<Object> returnList, String keyPath1, String keyPath2) {
        Object firstAPIValues = this.firstAPIObject;
        Object secondAPIValues = this.secondAPIObject;
        String[] firstAPISteps = keyPath1.split(">");
        String[] secondAPISteps = keyPath2.split(">");
        List<Object> duplicatesValue = new ArrayList<>();
        for (String s : firstAPISteps) {
            firstAPIValues = goIntoJSON(firstAPIValues, s);
        }
        for (String s : secondAPISteps) {
            secondAPIValues = goIntoJSON(secondAPIValues, s);
        }
        getDuplicatesValues(firstAPIValues, secondAPIValues, duplicatesValue);
        returnList = (prepareListToReturn(duplicatesValue));
        return returnList;
    }

    private void getDuplicatesValues(Object firstAPIValues, Object secondAPIValues, List<Object> duplicatesValue) {
        if (firstAPIValues instanceof ArrayList<?> firstArray) {
            if (secondAPIValues instanceof ArrayList<?> secondArray) {
                for (Object obj1 : firstArray) {
                    for (Object obj2 : secondArray) {
                        if (obj1.equals(obj2) && !duplicatesValue.contains(obj1)) {
                            this.duplicatesFound = true;
                            duplicatesValue.add(obj1);
                        }
                    }
                }
            }
        }
    }

    private List<Object> prepareListToReturn(List<Object> duplicatesValue) {
        List<Object> prepareValueToReturnList = new ArrayList<>();
        for(Object duplicate:duplicatesValue){
            JSONObject merged = new JSONObject();
            if (this.firstAPIObject != null) {
                Set<String> firstAPIKeys = this.firstAPIObject.keySet();
                firstAPIKeys.forEach((e) -> {
                    merged.put(e, this.firstAPIObject.get(e));
                });
            }
            if (this.secondAPIObject != null) {
                Set<String> secondAPIKeys = this.secondAPIObject.keySet();
                secondAPIKeys.forEach((e) -> {
                    merged.put(e, this.secondAPIObject.get(e));
                });
            }
            prepareValueToReturnList.add(removeDuplicates(merged, duplicatesValue, duplicate));
        }
        return prepareValueToReturnList;
    }

    private List<Object> removeDuplicates(JSONObject merged, List<Object> duplicatesValues, Object duplicate) {
        List<Object> removeDuplicatesList = new ArrayList<>();
                String[] firstAPISteps = this.keyPath1.split(">");
                String[] secondAPISteps = this.keyPath2.split(">");
                goIntoJSONToRemoveDuplicates(firstAPISteps, merged, duplicate);
                goIntoJSONToRemoveDuplicates(secondAPISteps, merged, duplicate);
                removeDuplicatesList.add(addDuplicateValue(merged, duplicatesValues, duplicate));
        return removeDuplicatesList;
    }

    private JSONObject addDuplicateValue(JSONObject merged, List<Object> duplicatesValues, Object duplicate) {
        List<Object> objectWhereDuplicatesCopy = new ArrayList<>(this.objectWhereDuplicates);
        if (!duplicatesValues.isEmpty()) {
            merged.put(this.replacementKeys.get(indexWhereFoundReplacementKey), duplicate);
            merged.put("numberOfDuplicates", objectWhereDuplicates.size());
            merged.put("objsWhereDuplicatesFound", objectWhereDuplicatesCopy);
        }
        this.objectWhereDuplicates.clear();
        return merged;
    }

    public Object goIntoJSON(Object obj, String nextStep) {
        Object returnValue = null;
        List<Object> moreValue = new ArrayList<>();
        returnValue = goIntoAnnidate(obj, moreValue, nextStep, returnValue);
        return returnValue;
    }

    public Object goIntoAnnidate(Object obj, List<Object> moreValue, String nextStep, Object returnValue) {
        if (obj instanceof ArrayList<?> arrayList) {
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
        } else if (obj instanceof JSONObject JSONObject) {
            returnValue = JSONObject.get(nextStep);
            this.mergedMap.put(nextStep, JSONObject.get(nextStep));
        }
        return returnValue;
    }

    public void goIntoJSONToRemoveDuplicates(String[] steps, Object mergedCopy, Object duplicate) {
        for (int i = 0; i < steps.length; i++) {
            if (i != steps.length - 1) {
                if (mergedCopy instanceof JSONObject jsonObject) {
                    mergedCopy = jsonObject.get(steps[i]);
                } else if (mergedCopy instanceof JSONArray jsonArray) {
                    for (Object obj : jsonArray) {
                        if (obj instanceof JSONObject jsonObject) {
                            goIntoJSONToRemoveDuplicates(Arrays.stream(steps, i, steps.length).toArray(String[]::new), jsonObject, duplicate);
                        }
                    }
                }
            } else {
                if (mergedCopy instanceof JSONObject jsonObject) {
                    if (duplicate.equals(jsonObject.get(steps[i]))) {
                        jsonObject.remove(steps[i]);
                        this.objectWhereDuplicates.add(jsonObject);
                    }
                } else if (mergedCopy instanceof JSONArray jsonArray) {
                    for (Object obj : jsonArray) {
                        if (obj instanceof JSONObject jsonObject) {
                            if (duplicate.equals(jsonObject.get(steps[i]))) {
                                jsonObject.remove(steps[i]);
                                this.objectWhereDuplicates.add(jsonObject);
                            }
                        }
                    }
                }
            }
        }
    }
}
