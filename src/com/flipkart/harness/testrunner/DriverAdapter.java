package com.flipkart.harness.testrunner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: sudhanshu.gupta
 * Date: 14/07/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DriverAdapter {
    public static Logger logger = Logger.getLogger(DriverAdapter.class.getName());
    public static WebDriver driver;
    static Properties configProperties = new Properties();
    public static String env;
    public String hostname = null, port = null, browser = null;

    public static boolean loadConfigFile() {
        env = System.getenv("ENV");

        if (env == null) {
            env = "local";
        }

        try {
            configProperties.load(new FileReader("conf/config.properties"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setup() {

        hostname = configProperties.getProperty("selenium.hostname");
        port = configProperties.getProperty("selenium.port");
        browser = configProperties.getProperty("selenium.browser");
        if(hostname==null && port==null && browser==null){
            try{
                hostname = "127.0.0.1";
                port = "4444";
                driver = new RemoteWebDriver(new URL("http://" + hostname + ":" + port + "/wd/hub"), DesiredCapabilities.firefox());
            } catch (MalformedURLException ex){
                ex.printStackTrace();
            }

        }else{

            try{
                if ( browser.startsWith("IE"))
                    driver = new RemoteWebDriver(new URL("http://" + hostname + ":" + port + "/wd/hub"), DesiredCapabilities.internetExplorer());
                else if ( browser.startsWith("FF"))
                    driver = new RemoteWebDriver(new URL("http://" + hostname + ":" + port + "/wd/hub"), DesiredCapabilities.firefox());
                else if (browser.startsWith("GoogleChrome"))
                    driver = new RemoteWebDriver(new URL("http://" + hostname + ":" + port), DesiredCapabilities.chrome());
                else
                    logger.info("Bad browser name: " + browser + ". Unable to launch");

            }catch (MalformedURLException ex){
                ex.printStackTrace();
            }
        }

    }

    public static WebDriver getDriver() {
        return driver;
    }

    public void teardown() {
        driver.close();
    }

}
