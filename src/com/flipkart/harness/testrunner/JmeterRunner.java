package com.flipkart.harness.testrunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.jmeter.NewDriver;

/**
 * Created by IntelliJ IDEA.
 * User: saikat
 * Date: 27/11/12
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class JmeterRunner implements HarnessRunner {

    Config config = new Config();
    String jmeter, testHome, testReport;
    Logger logger = LoggerFactory.getLogger(JmeterRunner.class);

    public JmeterRunner() {

        config.loadConfigFile();
        jmeter = config.configProperties.getProperty("jmeter.home");
        testHome = config.configProperties.getProperty("tests.home");
        testReport = config.configProperties.getProperty("output");

    }

    ArrayList<String> testList = new ArrayList<String>();

    public ArrayList<String> initialize(String module, String feature, String subfeature) {

        String testHome = config.configProperties.getProperty("tests.home");
        String path = testHome + "/" + module + "/" + feature + "/" + subfeature;
        File testDir = new File(path);
        if (testDir.list() != null) {
            String[] tests = testDir.list();

            for (int i = 0; i < tests.length; i++) {
                if (tests[i].contains(".jmx")) {
                    testList.add(tests[i]);
                }
            }

        } else {
            logger.info("No .jmx files were found.");
        }
        return testList;
    }


    public void execute(Test test, UUID batchId, Boolean persist) {

        try {

          logger.info("Test Filename "+test.getPath());
          ArrayList<String> argsArrayList = new ArrayList<String>();
          argsArrayList.add("-n");

          argsArrayList.add("-t");
          argsArrayList.add(test.getPath());
          argsArrayList.add("-l");
          argsArrayList.add("results.jtl");
          System.setProperty("jmeter.home",jmeter);
          String[] argsArray = argsArrayList.toArray(new String[argsArrayList.size()]);
          NewDriver.main(argsArray);



        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


}
