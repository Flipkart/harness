package tests.website.common.pages;


import java.io.FileInputStream;
import java.util.List;

import com.flipkart.harness.testng.Locator;
import com.flipkart.harness.testrunner.Config;
import com.flipkart.harness.utils.PageNamesEnum;
import tests.website.common.constants.HomePageConstants;
import com.flipkart.harness.utils.Common;
import com.flipkart.harness.utils.Locators;

public class HomePage extends Common{


    public boolean openHomePage()
    {
        openPage(config.configProperties.getProperty("app.server"));
        return true;
    }







}