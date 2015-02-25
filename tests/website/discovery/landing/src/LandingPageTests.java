package tests.website.discovery.landing.src;

import org.openqa.selenium.WebDriverException;
import tests.website.common.pages.HomePage;
import org.testng.annotations.Test;
import com.flipkart.harness.utils.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread;

import static com.flipkart.harness.testng.Assertion.*;

public class LandingPageTests extends BaseTest {
    Logger logger = LoggerFactory.getLogger(LandingPageTests.class);

    @Test
    public void verifyClothes() throws Exception {
        assertTrue(true, "Dummy Test passed");
    }
}
