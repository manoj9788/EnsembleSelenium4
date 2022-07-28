import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
//import org.openqa.selenium.devtools.v103.emulation.Emulation;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v103.emulation.Emulation;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GeoLocationTestOnGrid {

    private WebDriver driver;
    private WebDriverWait webDriverWait;
    private Actions actions;

    public String gridURL = "http://localhost:4444";


    @BeforeEach
    public void setUp() throws MalformedURLException {
        ChromeOptions browserOptions = new ChromeOptions();
        Map prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.geolocation", 1);
        browserOptions.setExperimentalOption("prefs", prefs);

        driver = new RemoteWebDriver(new URL( gridURL), browserOptions);
        Augmenter augmenter = new Augmenter();
        driver = augmenter.augment(driver);
    }

    @ParameterizedTest(name = "{index}. verify location from = {0} to {2}")
    @CsvSource(value = {
            "12.616761,80.199686,'Shore Temple Complex, Mahabalipuram, Path to beach, Mahabalipuram, Tirukalukundram, Chengalpattu District, Tamil Nadu, 603104, India'",
            "60.167605,24.954489,'Market Square, Kaartinkaupunki, Southern major district, Helsinki, Helsinki sub-region, Uusimaa, Southern Finland, Mainland Finland, 00170, Finland'",
            "37.774929,-122.419416,'Cistern at Kansas & Army, Market Street, Hayes Valley, San Francisco, California, 94102, United States'",
            "35.689487,139.691706,'Tokyo Metropolitan Government Office, Minami-dori Ave., Nishi-Shinjuku 2-chome, Shinjuku, Tokyo, 163-8001, Japan'",
            "-33.856215,151.215545,'Northern Boardwalk, Sydney, Council of the City of Sydney, New South Wales, 2000, Australia\n'",
    })
    public void verifyDistance(String latitude, String longitude,String Location_address) throws InterruptedException {

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Emulation.setGeolocationOverride(Optional.of(Float.parseFloat(latitude)),
                Optional.of(Float.parseFloat(longitude)),
                Optional.of(1)));

        driver.navigate().to("https://browserleaks.com/geo");

        WebElement map = driver.findElement(By.id("reverse"));
        Assertions.assertTrue(map.isDisplayed());

        String location_name = map.getText();
         System.out.println(location_name);

          System.out.println(Location_address);
        Assert.assertEquals(Location_address, location_name);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}