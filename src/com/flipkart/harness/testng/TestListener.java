package com.flipkart.harness.testng;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.flipkart.harness.testrunner.DriverAdapter;

import java.io.File;
import java.io.IOException;

public class TestListener extends TestListenerAdapter
{
    private int m_count = 0;

    @Override
    public void onTestFailure(ITestResult tr)
    {
        if(DriverAdapter.getDriver() != null)
        {
            WebDriver augmentedDriver = new Augmenter().augment(DriverAdapter.getDriver());
            File screenShot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
            try
            {
                FileUtils.copyFile(screenShot, new File(System.getProperty("testReport") + "/screenshots/"+tr.getMethod().getMethodName().toLowerCase()+".jpg"));
            }
            catch(IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        log("F");
        super.onTestFailure(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr)
    {
        log("S");
        super.onTestSkipped(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr)
    {
        log(".");
        super.onTestSuccess(tr);
    }

    private void log(String string)
    {
        System.out.print(string);
        if (++m_count % 10 == 0)
          System.out.println("");
    }

}
