import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v103.emulation.Emulation;

import java.util.Optional;

public class GeoLocationTestWithMultipleData {

    ChromeDriver driver;
    DevTools devtools;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
    }

    @ParameterizedTest(name = "{index}. verify location from = {0} to {2}")
    @CsvSource(value = {
            "12.616761,80.199686,'Shore Temple Complex, Mahabalipuram, Path to beach, Mahabalipuram, Tirukalukundram, Chengalpattu District, Tamil Nadu, 603104, India'",
            "60.167605,24.954489,'Market Square, Kaartinkaupunki, Southern major district, Helsinki, Helsinki sub-region, Uusimaa, Southern Finland, Mainland Finland, 00170, Finland'",
            "37.774929,-122.419416,'Cistern at Kansas & Army, Market Street, Hayes Valley, San Francisco, California, 94102, United States'",
            "35.689487,139.691706,'Tokyo Metropolitan Government Office, Minami-dori Ave., Nishi-Shinjuku 2-chome, Shinjuku, Tokyo, 163-8001, Japan'",
            "-33.856215,151.215545,'Northern Boardwalk, Sydney, Council of the City of Sydney, New South Wales, 2000, Australia\n'",
    })
    public void GeoLocationTestWithMultipleData(String latitude, String longitude,String Location_address) throws InterruptedException {

        driver.manage().window().maximize();
        devtools = driver.getDevTools();
        devtools.createSession();

        devtools.send(Emulation.setGeolocationOverride(Optional.of(Float.parseFloat(latitude)),
                Optional.of(Float.parseFloat(longitude)),
                Optional.of(1)));
        driver.navigate().to("https://browserleaks.com/geo");

        Thread.sleep(4000);

        WebElement map = driver.findElement(By.id("reverse"));
        Assertions.assertTrue(map.isDisplayed());

        String location_name = map.getText();

        Assert.assertTrue(Location_address, Location_address.contains(location_name));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}