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
    String replacementKey;
    List<String> testListKeys = new ArrayList<>();
    List<Object> testListobjs = new ArrayList<>();

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
        this.replacementKey = configuratorReader.getReplacementKey();
    }

    public JSONObject checkDuplicates() {
        for(Mapping m1:this.firstConfig.mapping){
            for(Mapping m2:this.secondConfig.mapping){
                Object firstAPIValues = this.firstAPIObject;
                Object secondAPIValues = this.secondAPIObject;
                String[] firstAPISteps = m1.getKeyPath().split(">");
                String[] secondAPISteps = m2.getKeyPath().split(">");
                List<Object> duplicatesValue = new ArrayList<>();
                for (String s : firstAPISteps) {
                    firstAPIValues = goIntoJSON(firstAPIValues, s);
                }
                for (String s : secondAPISteps) {
                    secondAPIValues = goIntoJSON(secondAPIValues, s);
                }
                if (firstAPIValues instanceof ArrayList<?> firstArray) {
                    if (secondAPIValues instanceof ArrayList<?> secondArray) {
                        for (Object obj1 : firstArray) {
                            for (Object obj2 : secondArray) {
                                if (obj1.equals(obj2)) {
                                    this.duplicatesFound = true;
                                    duplicatesValue.add(obj1);
                                }
                            }
                        }
                    }
                }
                return prepareValueToReturn(duplicatesValue);
            }
        }
        return null;
    }

    private JSONObject prepareValueToReturn(List<Object> duplicatesValue) {
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
        return removeDuplicates(merged, duplicatesValue);
    }

    private JSONObject removeDuplicates(JSONObject merged, List<Object> duplicatesValues) {
        List<String> firstAPISteps = List.of(this.firstConfig.keyWhereCheckDuplicate.split(">"));
        List<String> secondAPISteps = List.of(this.secondConfig.keyWhereCheckDuplicate.split(">"));
        goIntoJSONToRemoveDuplicates(firstAPISteps, merged, duplicatesValues);
        goIntoJSONToRemoveDuplicates(secondAPISteps, merged, duplicatesValues);
        return addDuplicateValue(merged, duplicatesValues);
    }

    private JSONObject addDuplicateValue(JSONObject merged, List<Object> duplicatesValues) {
        if (!duplicatesValues.isEmpty()) {
            merged.put(this.replacementKey, duplicatesValues.get(0));
            merged.put("numberOfDuplicates", duplicatesValues.size());
        }
        merged.put("objsWhereDuplicatesFound", testListobjs);
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
                        this.testListKeys.add(nextStep);
                    }
                } else {
                    goIntoAnnidate(objInJsonArray, moreValue, nextStep, returnValue);
                }
            }
            return moreValue;
        } else if (obj instanceof JSONObject JSONObject) {
            returnValue = JSONObject.get(nextStep);
            this.mergedMap.put(nextStep, JSONObject.get(nextStep));
            this.testListKeys.add(nextStep);
        }
        return returnValue;
    }

    public void goIntoJSONToRemoveDuplicates(List<String> steps, Object mergedCopy, List<Object> duplicatesValues) {
        for (int i = 0; i < steps.size(); i++) {
            if (i != steps.size() - 1) {
                if (mergedCopy instanceof JSONObject jsonObject) {
                    mergedCopy = jsonObject.get(steps.get(i));
                } else if (mergedCopy instanceof JSONArray jsonArray) {
                    for (Object obj : jsonArray) {
                        if (obj instanceof JSONObject jsonObject) {
                            goIntoJSONToRemoveDuplicates(steps.subList(i, steps.size()), jsonObject, duplicatesValues);
                        }
                    }
                }
            } else {
                if (mergedCopy instanceof JSONObject jsonObject) {
                    if (duplicatesValues.contains(jsonObject.get(steps.get(i)))) {
                        jsonObject.remove(steps.get(i));
                        this.testListobjs.add(jsonObject);
                    }
                } else if (mergedCopy instanceof JSONArray jsonArray) {
                    for (Object obj : jsonArray) {
                        if (obj instanceof JSONObject jsonObject) {
                            if (duplicatesValues.contains(jsonObject.get(steps.get(i)))) {
                                jsonObject.remove(steps.get(i));
                                this.testListobjs.add(jsonObject);
                            }
                        }
                    }
                }
            }
        }
    }
}
