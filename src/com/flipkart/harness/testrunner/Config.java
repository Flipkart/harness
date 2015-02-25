package com.flipkart.harness.testrunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {

    public static Properties configProperties = new Properties();

    public boolean loadConfigFile() {
        try {
            configProperties.load(new FileReader("conf/config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
