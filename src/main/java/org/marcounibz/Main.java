package org.marcounibz;

import org.json.simple.parser.ParseException;
import org.marcounibz.configurationHelper.ConfiguratorReader;

import java.io.IOException;

public class Main {

    static ConfiguratorReader configuratorReader = new ConfiguratorReader();


    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        dataManager.compareData();
    }
}