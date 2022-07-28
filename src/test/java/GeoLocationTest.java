import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v103.emulation.Emulation;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;


public class GeoLocationTest {
    ChromeDriver driver;
    DevTools devtools;

    private int lattitude;
    private int longitude;
    private int accuracy;
    private String expectedLocation;

    @BeforeAll

    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
    }
    @AfterEach
    void teardown() {
         driver.quit();
    }


    @Test
    public void GeoLocationTest1() throws IOException, InterruptedException {

        devtools = driver.getDevTools();
        devtools.createSession();

        devtools.send(Emulation.setGeolocationOverride(Optional.of(52.5043),
                Optional.of(13.4501),
                Optional.of(1)));

        driver.navigate().to("https://browserleaks.com/geo");

        /* To get the location into Viewport you could use JavaScript scroll or just use a click command from
        * selenium */

       // JavascriptExecutor js = (JavascriptExecutor) driver;
       // js.executeScript("javascript:window.scrollBy(250,350)");

        driver.findElement(By.cssSelector(".flag_text.wball")).click();

        String location_text = driver.findElement(By.cssSelector(".flag_text.wball")).getText();
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("./Screenshot.png"));

        Assert.assertTrue(location_text, location_text.contains("Brighter"));

    }
}
