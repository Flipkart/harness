package com.flipkart.harness.testrunner;

import com.flipkart.harness.jmeter.JmeterArgs;
import com.flipkart.harness.jmeter.JmeterResultsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        System.setProperty("jmeter.home",jmeter);

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

        DbHelper db = new DbHelper();
        try {
          String testFile = test.getPath();
          String reportFile = testReport + "/" + batchId + "/" + "Jmeter"+ "/" + test.getName() + "/" + test.getName() + ".jtl";

          NewDriver.main(JmeterArgs.buildArgsArray(testFile,reportFile));
          waitForTestComplete();
          test = JmeterResultsParser.parse(test,reportFile);


          db.addPerfResults(test, batchId, persist);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void waitForTestComplete(){
        Thread waitThread = null;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            if ("StandardJMeterEngine".equals(thread.getName())) {
                waitThread = thread;
                break;
            }

        }
        if (waitThread != null) {
            try{
            waitThread.join();
            }catch (Exception ex){ex.printStackTrace();}
        }
    }
}
