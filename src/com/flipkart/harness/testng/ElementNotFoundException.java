package com.flipkart.harness.testng;

public class ElementNotFoundException extends RuntimeException
{
    private String page;
    private Locator locator;

    public ElementNotFoundException(String page, Locator locator)
    {
        this.locator = locator;
        this.page = page;
    }

    @Override
    public String toString()
    {
        return page + " does not contain the element \"" + locator.key + "\" identified by locator: " + locator.value;
    }
}
