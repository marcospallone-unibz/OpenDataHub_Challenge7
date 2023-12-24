package org.marcounibz;

import netscape.javascript.JSObject;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        Map<JSONObject, JSONObject> mergedMap;
        mergedMap = dataManager.compareData();
        System.out.println(mergedMap);
        //dataManager.compareData();
    }
}