package org.marcounibz;

import org.json.simple.JSONObject;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        dataManager.checkDuplicates();
    }
}