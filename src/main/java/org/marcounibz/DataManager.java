package org.marcounibz;


import org.marcounibz.configurationHelper.ConfiguratorReader;
import org.marcounibz.mapping.OpenDataHubApiConfig;

import java.util.List;
import java.util.Map;

//this class will handle the datas and return
public class DataManager {
    Mobility mobility;
    Tourism tourism;
    OpenDataHubApiConfig mobilityConfig;
    OpenDataHubApiConfig tourismConfig;
    public DataManager(Mobility mobility, Tourism tourism){ //will they both be of type OpenDataHubClient???

        this.mobility = mobility;
        this.tourism = tourism;
    }

    //not sure about the position of this and the interaction between classes!
    public void setConfiguration(){
        ConfiguratorReader configuratorReader = new ConfiguratorReader();
        this.mobilityConfig = configuratorReader.getMobilityConfig();
        this.tourismConfig = configuratorReader.getTourismConfigConfig();
    }
    //get data from api and merge
    public List<Map<String, Object>> mergeData() throws Exception {
        List<Map<String, Object>> returnValue = null;
        List<Map<String, Object>> data1 = mobility.fetchDataFromApi(mobilityConfig);
        List<Map<String, Object>> data2 = tourism.fetchDataFromApi(tourismConfig);
        //merge somehow data

        return returnValue;
    }
}
