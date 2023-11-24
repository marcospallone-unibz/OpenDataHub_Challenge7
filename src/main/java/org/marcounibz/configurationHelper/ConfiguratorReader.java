package org.marcounibz.configurationHelper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.marcounibz.OpenDataHubApiClient;

//reads data in configuration.json
public class ConfiguratorReader {

    OpenDataHubApiConfig mobilityConfig; //i think they sould be called in a more generic way
    OpenDataHubApiConfig tourismConfig;


    public void readDataFromConfigurationFile(){
        //read data from configFile and save it in an obj of type OpenDataHubApiConfig and set mobility and tourismConfig
    }

    public OpenDataHubApiConfig getMobilityConfig(){
        OpenDataHubApiConfig copyOfMobilityConfig = this.mobilityConfig;
        return copyOfMobilityConfig;
    }
    public OpenDataHubApiConfig getTourismConfigConfig(){
        OpenDataHubApiConfig copyOfTourismConfig = this.tourismConfig;
        return copyOfTourismConfig;
    }

}
