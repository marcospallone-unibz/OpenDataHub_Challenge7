package org.marcounibz;

import org.json.simple.JSONObject;

import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        List<Object> returnList = dataManager.checkDuplicates();
        System.out.println(returnList);
    }
}