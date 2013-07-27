package com.flipkart.harness.jmeter;

/**
 * Created with IntelliJ IDEA.
 * User: saikat
 * Date: 15/03/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */



import com.flipkart.harness.testrunner.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JmeterResultsParser {

    static Logger logger = LoggerFactory.getLogger("Jmeter");

    static ArrayList<Integer> elapsedTime = new ArrayList<Integer>();
    static Integer samples = 0;
    static ArrayList<Integer> latency = new ArrayList<Integer>();
    static ArrayList<Long> timeStamp = new ArrayList<Long>();
    static Integer success = 0;
    static Integer failure = 0;
    static Integer minResponseTime,maxResponseTime,avgResponseTime,median,ninetiethPercentile;
    static long firstTs,lastTs,throughput;
    static double secondsElapsed;



    public static Test parse(Test test , String reportFile){

        final String REG_EX =
                "<httpSample\\s*" +
                        "t=\"([^\"]*)\"\\s*" +
                        "lt=\"([^\"]*)\"\\s*" +
                        "ts=\"([^\"]*)\"\\s*" +
                        "s=\"([^\"]*)\"\\s*" +
                        "lb=\"([^\"]*)\"\\s*" +
                        "rc=\"([^\"]*)\"\\s*" +
                        "rm=\"([^\"]*)\"\\s*" +
                        "tn=\"([^\"]*)\"\\s*" +
                        "dt=\"([^\"]*)\"\\s*"+
                        "by=\"([^\"]*)\"\\s*"+
                        ">";

        Pattern p = Pattern.compile(REG_EX);


        try{
            Path path = FileSystems.getDefault().getPath(reportFile);
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            for(String line : lines){

                //logger.info(line);


                Matcher m = p.matcher(line);

                while (m.find()) {
                    samples = samples+1;
                    elapsedTime.add(Integer.parseInt(m.group(1)));
                    latency.add(Integer.parseInt(m.group(2)));
                    timeStamp.add(Long.parseLong(m.group(3)));
                    if(m.group(4).equals("true")){
                        success = success + 1;
                    }else{
                        failure = failure + 1;
                    }

                }
            }
            Collections.sort(elapsedTime);

            logger.info("===============================================");
            logger.info("Test Name "+test.getName());
            logger.info("sample count "+samples);

            minResponseTime = getMinResponseTime(elapsedTime);
            logger.info("minResponseTime "+minResponseTime);
            test.setMinResponseTime(minResponseTime);

            maxResponseTime = getMaxResponseTime(elapsedTime);
            logger.info("maxResponseTime "+maxResponseTime);
            test.setMaxResponseTime(maxResponseTime);
            test.setSamples(samples);
            logger.info("success "+success);
            test.setSuccess(success);
            logger.info("failures "+failure);
            test.setFailure(failure);
            avgResponseTime = getAvgResponseTime(elapsedTime);
            logger.info("avgResponseTime "+avgResponseTime);
            test.setAvgResponseTime(avgResponseTime);
            median = getMedian(samples,elapsedTime);
            logger.info("median "+median);
            test.setMedian(median);
            ninetiethPercentile = getNinetiethPercentile(samples,elapsedTime);
            logger.info("ninetiethPercentile "+ninetiethPercentile);
            test.setNinetiethPercentile(ninetiethPercentile);
            Collections.sort(timeStamp);
            throughput = getThroughput(samples,timeStamp);
            logger.info("throughput "+throughput);
            test.setThroughput(throughput);

            logger.info("===============================================");


        }

        catch (Exception ex){
            ex.printStackTrace();
        }

        return test;


    }

    public static Integer getMinResponseTime(ArrayList<Integer> elapsedTime){
           return elapsedTime.get(0);
    }

    public static Integer getMaxResponseTime(ArrayList<Integer> elapsedTime){
        return elapsedTime.get(elapsedTime.size()-1);
    }

    public static Integer getAvgResponseTime(ArrayList<Integer> elapsedTime){
        Integer totalTime=0;
        for(Integer i : elapsedTime) {
            totalTime = totalTime+i;

        }

        avgResponseTime = totalTime/samples;
        return avgResponseTime;
    }

    public static Integer getMedian(Integer samples,ArrayList<Integer> elapsedTime){
        return elapsedTime.get(samples/2);
    }

    public static Integer getNinetiethPercentile(Integer samples,ArrayList<Integer> elapsedTime){

        double val = Math.floor(samples*.9);
        int ninetiethPercentileIndex = (int) val;
        return elapsedTime.get(ninetiethPercentileIndex-1);

    }

    public static long getThroughput(Integer samples,ArrayList<Long> timeStamp){
        firstTs = timeStamp.get(0);
        lastTs = timeStamp.get(timeStamp.size()-1);
        secondsElapsed = (lastTs - firstTs)/1000.00;
        throughput = Math.round(samples/secondsElapsed);
        return throughput;
    }


}



