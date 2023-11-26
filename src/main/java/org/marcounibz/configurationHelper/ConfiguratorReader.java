package org.marcounibz.configurationHelper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.marcounibz.OpenDataHubApiClient;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//reads data in configuration.json
public class ConfiguratorReader {
    JSONParser parser = new JSONParser();

    OpenDataHubApiConfig mobilityConfig; //i think they sould be called in a more generic way
    OpenDataHubApiConfig tourismConfig;


    public void readDataFromConfigurationFile() throws IOException, ParseException {
        //read data from configFile and save it in an obj of type OpenDataHubApiConfig and set mobility and tourismConfig
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("c:\\exer4-courses.json"));

        for (Object o : jsonArray)
        {
            JSONObject person = (JSONObject) o;

            String name = (String) person.get("name");

            String city = (String) person.get("city");
            System.out.println(city);

            String job = (String) person.get("job");
            System.out.println(job);

            JSONArray cars = (JSONArray) person.get("cars");

            for (Object c : cars)
            {
                System.out.println(c+"");
            }
        }
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
