package com.flipkart.harness.utils;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BaseTest {
    Common common;

    @BeforeSuite
    public void setup() throws Exception {
        common = new Common();
        common.setup();
    }

    @AfterSuite
    public void teardown()
    {
        common.teardown();
    }

    protected int getRandomNumberBetween(int min, int max)
    {
        return new Random().nextInt(max - min) + min;
    }
}
