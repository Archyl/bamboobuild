package utils;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebDriverUtil {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverUtil.class);
    static final String DRIVER_PATH = System.getProperty("project.basedir")
            + "/src/main/resources/chrome/";
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static Map<Long, ChromeDriverService> serviceMap = new ConcurrentHashMap<>();
    private static Long currentThreadId;

    private WebDriverUtil() {

    }

    public static WebDriver getDriver() {
        if (DRIVER.get() != null) {
            return DRIVER.get();
        }
        try {
            LOG.info("Driver init...");
            DRIVER.set(createDriver());
            DRIVER.get().manage().window().maximize();

            return DRIVER.get();
        } catch (Exception e) {
            LOG.info("Failed to start driver.", e.getMessage());
            throw e;
        }
    }

    public static void stopService() {
        ResultKeeper<ChromeDriverService> container = extractCurrentService(currentThreadId);
        if (container.hasResult()) {
            if (container.getResult().isRunning()) {
                container.getResult().stop();
            }
        }
    }

    public static void setupSelenide(WebDriver webDriver) {
        WebDriverRunner.setWebDriver(webDriver);
        com.codeborne.selenide.Configuration.timeout = 8000;
        com.codeborne.selenide.Configuration.screenshots = true;
    }

    public static WebDriver createDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--no-proxy-server");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-translate");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--verbose");
        options.addArguments("--disable-impl-side-painting");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability("credentials_enable_service", false);
        capabilities.setCapability("password_manager_enabled", false);
        capabilities.setCapability("Options_PasswordManager", false);
        WebDriver driver = new RemoteWebDriver(getChromeDriverService().getUrl(), capabilities);
        setupSelenide(driver);
        return driver;
    }

    private static ChromeDriverService getChromeDriverService() {
        ResultKeeper<ChromeDriverService> container = extractCurrentService();
        if (!extractCurrentService().hasResult()) {
            container.put(startChromeService());
        }
        return container.getResult();
    }

    private static ResultKeeper<ChromeDriverService> extractCurrentService() {
        currentThreadId = Thread.currentThread().getId();
        return extractCurrentService(currentThreadId);
    }

    private static ResultKeeper<ChromeDriverService> extractCurrentService(Long threadId) {
        ResultKeeper<ChromeDriverService> container = new ResultKeeper<>();
        ChromeDriverService service = serviceMap.get(threadId);
        if (service != null) {
            container.put(service);
        }
        return container;
    }

    private static void registerService(ChromeDriverService service) {
        serviceMap.put(currentThreadId, service);
    }

    private static ChromeDriverService startChromeService() {
        if (isMac()) {
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "mac/chromedriver");
        } else if (isWindows()) {
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "win/chromedriver");
        } else {
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "linux/chromedriver");
        }
        File chromeDriver;
        if (isMac()) {
            chromeDriver = new File(DRIVER_PATH + "mac/chromedriver");
        } else {
            chromeDriver = new File(DRIVER_PATH + "linux/chromedriver");
        }
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(chromeDriver)
                .usingAnyFreePort().withVerbose(true).build();
        try {
            service.start();
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Unable to start chrome driver from location '%s': \n %s",
                    chromeDriver.getAbsolutePath(), e.getMessage()));
        }
        registerService(service);
        return extractCurrentService().getResult();
    }

    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }


}
