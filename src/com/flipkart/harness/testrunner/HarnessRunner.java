package com.flipkart.harness.testrunner;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: saikat
 * Date: 21/11/12
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface HarnessRunner {

    public ArrayList<String> initialize(String module, String feature, String subfeature);

    public void execute(Test test, UUID batchId,Boolean persist);

}
