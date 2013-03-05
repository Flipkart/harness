package com.flipkart.harness.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.flipkart.harness.testng.ElementNotFoundException;
import com.flipkart.harness.testng.Locator;
import com.flipkart.harness.testrunner.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Common {

    public static Logger logger = Logger.getLogger(Common.class.getName());
    public static WebDriver driver;
    static Properties configProperties = new Properties();
    public static Properties props = new Properties();
    public static String ProjectPath;
    public static String LocatorsPath;
    public static String env;
    public static FileInputStream fis;
    public Map<String, Locator> locators;

    public String hostname = null, port = null, browser = null;
    protected Config config = new Config();


    public static boolean loadConfigFile() {
        env = System.getenv("ENV");

        if (env == null) {
            env = "local";
        }

        try {
            configProperties.load(new FileReader("conf/config.properties"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setup() {

        hostname = configProperties.getProperty("selenium.hostname");
        port = configProperties.getProperty("selenium.port");
        browser = configProperties.getProperty("selenium.browser");

        if(hostname==null && port==null && browser==null){

            driver = new FirefoxDriver();

        }else{

            try{
            if ( browser.startsWith("IE"))
                driver = new RemoteWebDriver(new URL("http://" + hostname + ":" + port + "/wd/hub"), DesiredCapabilities.internetExplorer());
            else if ( browser.startsWith("FF"))
                driver = new RemoteWebDriver(new URL("http://" + hostname + ":" + port + "/wd/hub"), DesiredCapabilities.firefox());
            else if (browser.startsWith("GoogleChrome"))
                driver = new RemoteWebDriver(new URL("http://" + hostname + ":" + port + "/wd/hub"), DesiredCapabilities.chrome());
            else
                logger.info("Bad browser name: " + browser + ". Unable to launch");

            }catch (MalformedURLException ex){
                ex.printStackTrace();
            }
            }

        }




    public void teardown() {
        driver.close();
    }

    public By getBy(String locator) {
        By by;
        if (locator.startsWith("//"))
            by = By.xpath(locator);
        else if (locator.startsWith("css="))
            by = By.cssSelector(locator.replace("css=", "").trim());
        else if (locator.startsWith("link="))
            by = By.linkText(locator.replace("link=", ""));
        else
            by = By.id(locator);
        return by;
    }

    public boolean isElementPresent(By by) throws Exception {
        List allElements = driver.findElements(by);
        if ((allElements == null) || (allElements.size() == 0))
            return false;
        else
            return true;
    }

    public boolean isAlphaNumeric(String Text) throws Exception {
        return Text.replaceAll(" ", "").replaceAll("\n", "").matches("[-+=!@#$%^&*()\\[.*?\\]{}_/.,;:'\"A-Za-z0-9]*");
    }

    public int roundUp(double d) {
        return (d > (int) d) ? (int) d + 1 : (int) d;
    }

    public boolean waitForElementPresent(By by, long TimeOutInMilliSec) throws Exception {
        long time = 0;
        while (time < TimeOutInMilliSec) {
            if (!isElementPresent(by)) {
                Thread.sleep(1000);
                time += 1000;
            } else
                return true;
        }
        return false;
    }

    public boolean waitForElementVisible(By by, long TimeOutInMilliSec) throws Exception {
        long time = 0;
        while (time < TimeOutInMilliSec) {
            if (!isElementPresent(by) || !driver.findElement(by).isDisplayed()) {
                Thread.sleep(1000);
                time += 1000;
            } else
                return true;
        }
        return false;
    }

    public boolean waitForElementNotPresent(By by, long TimeOutInMilliSec) throws Exception {
        long time = 0;
        while (time < TimeOutInMilliSec) {
            if (isElementPresent(by)) {
                Thread.sleep(1000);
                time += 1000;
            } else
                return true;
        }
        return false;
    }

    public boolean waitForElementInvisible(By by, long TimeOutInMilliSec) throws Exception {
        long time = 0;
        String s;
        try {
            while (time < TimeOutInMilliSec) {
                if (!isElementPresent(by))
                    return true;
                s = driver.findElement(by).getAttribute("style");
                if (!s.contains("hidden") || !s.contains("none")) {
                    Thread.sleep(1000);
                    time += 1000;
                } else
                    return true;
            }
        } catch (NoSuchElementException NE) {
            return true;
        }
        return false;
    }

    public void openPage(String domain, String href) {
        driver.get(domain + href);
    }

    public Alert getAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            Thread.sleep(2000);
            System.out.println("There is an alert: " + alert.getText());
            return alert;
        } catch (Exception e) {
            return null;
        }
    }

    public void hoverOver(String locator) {
        WebElement element = driver.findElement(getBy(props.getProperty(locator)));
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
    }

    public void mouseOver(WebElement element) {

        Locatable hoverItem = (Locatable) element;
        Mouse mouse = ((HasInputDevices) driver).getMouse();
        mouse.mouseMove(hoverItem.getCoordinates());

    }

    public void openPage(String url) {
        driver.navigate().to(url);
    }

    private WebElement findElement(Locator locator) {
        try {
            return driver.findElement(getBy(locator.value));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            throw new ElementNotFoundException(elements[2].getClassName(), locator);
        }
    }

    public List<WebElement> findElements(Locator locator) {
        return driver.findElements(getBy(locator.value));
    }

    public void click(Locator locator) {
        findElement(locator).click();
    }

    public void type(Locator locator, String stringToType) {
        findElement(locator).sendKeys(stringToType);
    }

    public boolean isElementPresent(Locator locator) {
        List allElements = findElements(locator);
        if ((allElements == null) || (allElements.size() == 0))
            return false;
        else
            return true;
    }

    public String getText(Locator locator) {
        return findElement(locator).getText().trim();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public void clearText(Locator locator) {
        if (isElementPresent(locator))
            findElement(locator).clear();
    }

    public String getAttribute(Locator locator, String attribute) {
        return findElement(locator).getAttribute(attribute);
    }

    public int getElementCount(Locator locator) {
        return findElements(locator).size();
    }

    public Point getElementLocationOnPage(Locator locator) {
        return findElement(locator).getLocation();
    }

    public Dimension getElementSizeOnPage(Locator locator) {
        return findElement(locator).getSize();
    }

    public String getTagName(Locator locator) {
        return findElement(locator).getTagName();
    }

    public boolean isElementVisible(Locator locator) {
        return findElement(locator).isDisplayed();
    }

    public boolean isEnabled(Locator locator) {
        return findElement(locator).isEnabled();
    }

    public boolean isSelected(Locator locator) {
        return findElement(locator).isSelected();
    }

    public void hoverOver(Locator locator) {
        WebElement element = findElement(locator);
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
    }

    public List<String> getAttributeOfAllElements(Locator locator, String attribute) {
        List<String> attributeList = new ArrayList<String>();
        List<WebElement> elements = findElements(locator);
        for (WebElement element : elements) {
            attributeList.add(element.getAttribute(attribute));
        }
        return attributeList;
    }

    public List<String> getTextOfAllElements(Locator locator) {
        List<String> textList = new ArrayList<String>();
        List<WebElement> elements = findElements(locator);
        for (WebElement element : elements) {
            textList.add(element.getText().trim());
        }
        return textList;
    }

    public void setDefaultTimeout(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    public void close() {
        driver.close();
    }

    public void waitForElementPresent(Locator locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.presenceOfElementLocated(getBy(locator.value)));
    }

    public void waitForElementVisible(Locator locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(locator.value)));
    }

    public void waitForElementNotVisible(Locator locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(getBy(locator.value)));
    }

    public void waitForElementNotPresent(Locator locator, int seconds) {
        int sec = seconds;
        do {
            if (!isElementPresent(locator))
                break;
            waitForSeconds(1);
            sec--;
        } while (sec > 0);
    }

    public void selectFromDropDown(Locator locator, String option) {
        Select dropList = new Select(findElement(locator));
        dropList.selectByVisibleText(option);
    }

    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public String generateString(int length) {
        Random rand = new Random();
        String characters = "qwertyuiopasdfghjklzxcvbnm", randomString = "";
        randomString = randomString + characters.toUpperCase().charAt(rand.nextInt(characters.length()));
        for (int i = 1; i < length; i++)
            randomString = randomString + characters.charAt(rand.nextInt(characters.length()));
        return randomString.trim();
    }

    private String convertedToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfOfByte = (data[i] >>> 4) & 0x0F;
            int twoHalfBytes = 0;
            do {
                if ((0 <= halfOfByte) && (halfOfByte <= 9)) {
                    buf.append((char) ('0' + halfOfByte));
                } else {
                    buf.append((char) ('a' + (halfOfByte - 10)));
                }
                halfOfByte = data[i] & 0x0F;
            } while (twoHalfBytes++ < 1);
        }
        return buf.toString();
    }

    public String MD5(String text) {
        byte[] md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md5 = new byte[64];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            md5 = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
        }
        return convertedToHex(md5);
    }

    public boolean isDisplayed(Locator locator) {
        return findElement(locator).isDisplayed();
    }


}
