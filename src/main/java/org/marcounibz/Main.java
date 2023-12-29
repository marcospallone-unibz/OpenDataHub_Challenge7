package org.marcounibz;

import org.json.simple.JSONObject;

public class Main {

    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        JSONObject jsonObject;
        jsonObject = dataManager.checkDuplicates();
        System.out.println(jsonObject);
    }
}