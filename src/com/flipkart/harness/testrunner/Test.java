package com.flipkart.harness.testrunner;

/**
 * Created by IntelliJ IDEA.
 * User: saikat
 * Date: 22/11/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getException() {
        return exception;
    }

    public void setException(int exception) {
        this.exception = exception;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getSubfeature() {
        return subfeature;
    }

    public void setSubfeature(String subfeature) {
        this.subfeature = subfeature;
    }
    public Integer getSamples() {
        return samples;
    }

    public void setSamples(Integer samples) {
        this.samples = samples;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Integer minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Integer maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Integer getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(Integer avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public Integer getMedian() {
        return median;
    }

    public void setMedian(Integer median) {
        this.median = median;
    }

    public Integer getNinetiethPercentile() {
        return ninetiethPercentile;
    }

    public void setNinetiethPercentile(Integer ninetiethPercentile) {
        this.ninetiethPercentile = ninetiethPercentile;
    }

    public long getThroughput() {
        return throughput;
    }

    public void setThroughput(long throughput) {
        this.throughput = throughput;
    }

    String name;
    String path;
    String type;
    int passed;
    int failed;
    int exception;
    String batchId;
    String browser;
    String module;
    String feature;
    String subfeature;
    Integer samples;
    Integer success;
    Integer failure;
    Integer minResponseTime;
    Integer maxResponseTime;
    Integer avgResponseTime;
    Integer median;
    Integer ninetiethPercentile;
    long throughput;


}
