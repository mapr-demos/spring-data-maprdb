package com.mapr.springframework.data.maprdb.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static Properties getProperties(String path) {
        Properties p = new Properties();
        try {
            p.load(new FileReader(new File("src/test/resources/tests.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

}
