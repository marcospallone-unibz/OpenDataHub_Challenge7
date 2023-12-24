package org.marcounibz;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        Map<Object, Object[]> mergedMap;
        mergedMap = dataManager.compareData();
        System.out.println(mergedMap);
        //dataManager.compareData();
    }
}