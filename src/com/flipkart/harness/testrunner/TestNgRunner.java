package com.flipkart.harness.testrunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestNgRunner implements HarnessRunner {

    Config config = new Config();
    String testHome, testReport;
    Logger logger = LoggerFactory.getLogger(TestNgRunner.class);
    public TestNgRunner() {

        config.loadConfigFile();
        testHome = config.configProperties.getProperty("tests.home");
        testReport = config.configProperties.getProperty("output");

    }

    ArrayList<String> testList = new ArrayList<String>();

    public ArrayList<String> initialize(String module, String feature, String subfeature) {

        String testHome = config.configProperties.getProperty("tests.home");
        String path = testHome + "/" + module + "/" + feature + "/" + subfeature;
        File testDir = new File(path+"/scripts");
        if (testDir.list() != null) {
            String[] tests = testDir.list();

            for (int i = 0; i < tests.length; i++) {
                if (tests[i].contains(".xml"))
                    testList.add(tests[i]);
            }
        } else {
            logger.info("No TestNG tests found ....");
        }
        return testList;
    }


    public void execute(Test test, UUID batchId,Boolean persist) {

        DbHelper db = new DbHelper();
        try {

           TestListenerAdapter tla = new TestListenerAdapter();
           ArrayList<String> suiteFiles = new ArrayList<String>();
           suiteFiles.add(test.getPath());
           XmlSuite xmlSuite = new XmlSuite();
           xmlSuite.setSuiteFiles(suiteFiles);
           TestNG testNG = new TestNG();
           testNG.addListener(tla);
           testNG.setCommandLineSuite(xmlSuite);
           testNG.setOutputDirectory(testReport + "/" + batchId + "/" + "TestNG");
           testNG.run();

           List failed = tla.getFailedTests();
           List passed = tla.getPassedTests();
           List ex = tla.getSkippedTests();
           test.setPassed(passed.size());
           test.setFailed(failed.size());
           test.setException(ex.size());
           db.addResults(test, batchId, persist);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }


    }

}
