package org.marcounibz;

import org.json.simple.parser.ParseException;
import org.marcounibz.configurationHelper.ConfiguratorReader;

import java.io.IOException;

public class Main {

    static ConfiguratorReader configuratorReader = new ConfiguratorReader();
    Mobility mobility = new Mobility(configuratorReader.getMobilityConfig());

    public static void main(String[] args) throws Exception {

        configuratorReader.readDataFromConfigurationFile();
        Tourism tourism = new Tourism(configuratorReader.getTourismConfigConfig());
        tourism.fetchDataFromApi();
        tourism.splitPath();

    }
}