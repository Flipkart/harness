package com.flipkart.harness.testrunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

public class FolderScanner {

    private static FolderScanner instance = null;
    static Config config;
    ArrayList<Test> testList = new ArrayList<Test>();

    Logger logger = LoggerFactory.getLogger(FolderScanner.class);

    protected FolderScanner() {

        config = new Config();
        config.loadConfigFile();
    }


    public static FolderScanner getInstance() {

        if (instance == null) {
            instance = new FolderScanner();
        }
        return instance;
    }

    public ArrayList<Test> scan(String module, String feature, String subfeature) {



            String testHome = config.configProperties.getProperty("tests.home");
            String path = testHome + "/" + module;

            //logger.info("path is "+path);
            String[] featureList = getSubFolderList(path);

            for (int i = 0; i < featureList.length; i++) {

                if (!featureList[i].equals(".DS_Store") && featureList[i].equals(feature)) {
                    String featurePath = path + "/" + featureList[i];
                    scanFeature(module, featureList[i], featurePath, subfeature);

                } else if (!featureList[i].equals(".DS_Store") && feature.equals("")) {
                    String featurePath = path + "/" + featureList[i];
                    scanFeature(module, featureList[i], featurePath, subfeature);

                }


            }

        return testList;
    }


    void scanFeature(String module, String feature, String path, String subfeature) {
        String[] subfeatureList = getSubFolderList(path);
        for (int j = 0; j < subfeatureList.length; j++) {
            if (!subfeatureList[j].equals(".DS_Store") && subfeatureList[j].equals(subfeature)) {


                scanSubFeature(module, feature, subfeatureList[j]);

            } else if (!subfeatureList[j].equals(".DS_Store") && subfeature.equals("")) {


                scanSubFeature(module, feature, subfeatureList[j]);

            }

        }

    }

    void scanSubFeature(String module, String feature, String subfeature) {
        ArrayList<String> tList = new ArrayList<String>();

        HarnessRunner hr = new TestNgRunner();
        tList = hr.initialize(module, feature, subfeature);

        String testHome = config.configProperties.getProperty("tests.home");
        String path = testHome + "/" + module + "/" + feature + "/" + subfeature + "/" + "scripts";
        for (String t : tList) {
            Test test = new Test();
            test.setName(t.replace(".xml", ""));
            test.setPath(path + "/" + t);
            test.setType("TestNg");
            test.setModule(module);
            test.setFeature(feature);
            test.setSubfeature(subfeature);

            testList.add(test);
        }


        hr = new JmeterRunner();
        tList = hr.initialize(module, feature, subfeature);
        path = testHome + "/" + module + "/" + feature + "/" + subfeature;

        for (String t : tList) {
            Test test = new Test();
            test.setName(t.replace(".jmx", ""));
            test.setPath(path + "/" + t);
            test.setType("Jmeter");
            test.setModule(module);
            test.setFeature(feature);
            test.setSubfeature(subfeature);

            testList.add(test);
        }


    }

    public static String[] getSubFolderList(String path) {
        File moduleDir = new File(path);
        String[] folderList = moduleDir.list();
        return folderList;
    }

}
