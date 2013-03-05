package com.flipkart.harness.testng;

import org.testng.Assert;
import java.util.Collection;
import java.util.Map;

public class Assertion
{

    public static void assertEquals(String actual, String expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(int actual, int expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(Object actual, Object expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(boolean actual, boolean expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(long actual, long expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(Collection actual, Collection expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(char actual, char expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(Map actual, Map expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(float actual, float expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(double actual, double expected, String errorMessage)
    {
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertFalse(boolean condition, String errorMessage)
    {
        Assert.assertFalse(condition, errorMessage);
    }

    public static void assertTrue(boolean condition, String errorMessage)
    {
        Assert.assertTrue(condition, errorMessage);
    }

    public static void assertNull(Object object, String errorMessage)
    {
        Assert.assertNull(object, errorMessage);
    }

    public static void assertNotNull(Object object, String errorMessage)
    {
        Assert.assertNotNull(object, errorMessage);
    }

    public static void fail(String errorMessage)
    {
        Assert.fail(errorMessage);
    }

}
