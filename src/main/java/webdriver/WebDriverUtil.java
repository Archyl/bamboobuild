package webdriver;

import utils.properties.PropertiesService;

import static com.codeborne.selenide.Selenide.open;
import static utils.properties.PropertiesConstants.BAMBOO_FRONT_URL;
import static utils.properties.PropertiesConstants.CHROME_DRIVER_VARIABLE;
import static utils.properties.PropertiesConstants.CHROME_LINUX_PATH;
import static utils.properties.PropertiesConstants.CHROME_MAC_PATH;
import static utils.properties.PropertiesConstants.CHROME_WIN_PATH;
import static utils.properties.PropertiesConstants.SELENIDE_BROWSER_VARIABLE;

public class WebDriverUtil {

    private static final String DRIVER_PATH = System.getProperty("project.basedir")
            + "/src/main/resources/chrome/";

    private WebDriverUtil() {

    }

    public static void startDriver() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty(PropertiesService.getProperty(CHROME_DRIVER_VARIABLE), DRIVER_PATH +
                    PropertiesService.getProperty(CHROME_MAC_PATH));
        } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
            System.setProperty(PropertiesService.getProperty(CHROME_DRIVER_VARIABLE), DRIVER_PATH +
                    PropertiesService.getProperty(CHROME_WIN_PATH));
        } else {
            System.setProperty(PropertiesService.getProperty(CHROME_DRIVER_VARIABLE), DRIVER_PATH +
                    PropertiesService.getProperty(CHROME_LINUX_PATH));
        }
        System.setProperty(PropertiesService.getProperty(SELENIDE_BROWSER_VARIABLE), "chrome");
        open(PropertiesService.getProperty(BAMBOO_FRONT_URL));
    }

}
