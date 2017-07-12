package buildrun;

import helpers.BuildPageHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pages.LoginPage;
import utils.properties.PropertiesService;
import webdriver.WebDriverUtil;

import static com.codeborne.selenide.Selenide.open;
import static utils.properties.PropertiesConstants.BAMBOO_PASSSWORD;
import static utils.properties.PropertiesConstants.BAMBOO_USER;
import static utils.properties.PropertiesConstants.DEPLOY_URL;

@RunWith(JUnit4.class)
public class RunBuildTest {



    private LoginPage loginPage;
    private BuildPageHelper buildPageHelper = new BuildPageHelper();

    public RunBuildTest() {
        loginPage = new LoginPage();
    }

    @BeforeClass
    public static void startDriver() {
        WebDriverUtil.startDriver();
    }

    @Test
    public void runBuildTest() {
        loginPage.loginToBamboo(PropertiesService.getProperty(BAMBOO_USER),
                PropertiesService.getProperty(BAMBOO_PASSSWORD));
        open(PropertiesService.getProperty(DEPLOY_URL));
        buildPageHelper.startDeploy();
    }

}
