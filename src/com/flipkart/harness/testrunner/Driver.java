package com.flipkart.harness.testrunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: saikat
 * Date: 21/11/12
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Driver {

    static String module, feature, subfeature;
    static ArrayList<Test> tests;
    static ArrayList<Test> testNgBucket = new ArrayList<Test>();
    static ArrayList<Test> jmeterBucket = new ArrayList<Test>();
    static HashMap<HarnessRunner, ArrayList> testMap = new HashMap<HarnessRunner, ArrayList>();
    static UUID batchId = UUID.randomUUID();
    static Boolean persist;

    public static void main(String args[]) throws Exception {

        module = System.getProperty("module");
        feature = System.getProperty("feature");
        subfeature = System.getProperty("subfeature");
        persist = Boolean.parseBoolean(System.getProperty("persist"));

        FolderScanner fc = FolderScanner.getInstance();
        tests = fc.scan(module, feature, subfeature);

        createTestBucket(tests);

        startExecution(testMap,persist);

    }

    static void createTestBucket(ArrayList<Test> tests) {

        for (Test t : tests) {
             if (t.type.equals("TestNg")) {
                testNgBucket.add(t);
            } else if (t.type.equals("Jmeter")) {
                jmeterBucket.add(t);
            }
        }
        HarnessRunner tr = new TestNgRunner();
        HarnessRunner jr = new JmeterRunner();

        testMap.put(tr, testNgBucket);
        testMap.put(jr, jmeterBucket);

    }


    static void startExecution(final HashMap<HarnessRunner, ArrayList> testMap,final Boolean persist) {

        for (Map.Entry<HarnessRunner, ArrayList> entry : testMap.entrySet()) {
            final HarnessRunner hr = entry.getKey();
            final ArrayList<Test> tList = entry.getValue();
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {

                        for (Test t : tList) {
                            hr.execute(t, batchId, persist);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();

        }

    }

}


