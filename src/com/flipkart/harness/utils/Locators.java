package com.flipkart.harness.utils;

import com.flipkart.harness.testng.Locator;
import com.flipkart.harness.testrunner.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Locators
{
    static Map<PageNamesEnum, String> paths = new HashMap<PageNamesEnum, String>();
    static Map<PageNamesEnum, Map<String, Locator>> locatorsMap = new HashMap<PageNamesEnum, Map<String, Locator>>();

    static
    {
        for(PageNamesEnum pageName : PageNamesEnum.values())
        {
            Config config = new Config();
            config.loadConfigFile();
            paths.put(pageName, config.configProperties.getProperty("locators.home")+ "/" + "locators.properties");
        }
    }

    public static Map<String, Locator> getLocators(PageNamesEnum pageName)
    {
        Map<String, Locator> locators = locatorsMap.get(pageName);

        if(locators != null)
            return locators;

        locators = new HashMap<String, Locator>();
        Properties props = new Properties();
        try
        {
            FileInputStream fis = new FileInputStream(paths.get(pageName));
            props.load(fis);
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Enumeration em = props.keys();
        while (em.hasMoreElements())
        {
            String key = em.nextElement().toString();
            locators.put(key, new Locator(key, props.getProperty(key)));
        }

        locatorsMap.put(pageName, locators);

        return locators;
    }
}
