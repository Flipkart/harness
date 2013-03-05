package com.flipkart.harness.testrunner;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

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

    public JmeterRunner() {

        config.loadConfigFile();
        jmeter = config.configProperties.getProperty("jmeter.home") + "/bin/jmeter";
        testHome = config.configProperties.getProperty("tests.home");
        testReport = config.configProperties.getProperty("tests.report");

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
            System.out.println("No .jmx files required for Jmeter tests were found.");
        }
        return testList;
    }


    public void execute(Test test, UUID batchId, Boolean persist) {



        int right = 0, wrong = 0, exceptions = 0;
        try {
            Process p = Runtime.getRuntime().exec(jmeter + " -n -t " + test.path + " -l " + testReport + "/" + batchId + "/Jmeter/" + test.name + ".csv");
            p.waitFor();

            String line;

            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = error.readLine()) != null) {
                System.out.println(line);
            }
            error.close();

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {

                System.out.println(line);
            }

            input.close();

            OutputStream outputStream = p.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println();
            printStream.flush();
            printStream.close();

            test.setPassed(1);
            test.setFailed(wrong);
            test.setException(exceptions);

        } catch (Exception e) {

            test.setPassed(right);
            test.setFailed(wrong);
            test.setException(1);


            e.printStackTrace();
        } finally {

        }

    }


}
