package org.marcounibz;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        Tourism tourism = new Tourism();
        tourism.getData();
    }
}