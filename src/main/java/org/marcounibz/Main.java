package org.marcounibz;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        Tourism tourism = new Tourism();
        Mobility mobility = new Mobility();

        JSONObject tourismData;
        JSONObject mobilityData;

        tourismData = tourism.getData();
        mobilityData = mobility.getDataFromApi();

        System.out.println(tourismData);
        System.out.println("\n \n \n -----SEPARATE----- \n \n \n");
        System.out.println(mobilityData);
    }
}