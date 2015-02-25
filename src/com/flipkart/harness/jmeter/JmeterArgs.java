package com.flipkart.harness.jmeter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: saikat
 * Date: 15/03/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class JmeterArgs {

    static String noGui = "-n";


    public static String[] buildArgsArray(String testFile,String reportFile){

        ArrayList<String> argsArrayList = new ArrayList<String>();

        argsArrayList.add(noGui);
        argsArrayList.add("-t");
        argsArrayList.add(testFile);
        argsArrayList.add("-l");
        argsArrayList.add(reportFile);

        String[] argsArray = argsArrayList.toArray(new String[argsArrayList.size()]);
        return argsArray;
    }
}
