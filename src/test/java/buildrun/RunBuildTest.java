package buildrun;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;
import pages.BuildPage;
import pages.LoginPage;
import utils.WebDriverUtil;

@RunWith(JUnit4.class)
public class RunBuildTest {

    private static final String BAMBOO_URL = "https://amway-prod.tt.com.pl/bamboo/allPlans.action";
    private static final String URL_TO_DEPLOY_FQA1 = "https://amway-prod.tt.com.pl/bamboo/deploy"
            + "/selectVersionForExecute.action?environmentId=2326531&returnUrl=/deploy"
            + "/viewAllDeploymentProjects.action";
    private static final String URL_TO_DEPLOY_FQA2 = "https://amway-prod.tt.com.pl/bamboo/deploy"
            + "/selectVersionForExecute.action?environmentId=2326533&returnUrl=/deploy"
            + "/viewAllDeploymentProjects.action";
    private static final String URL_TO_DEPLOY_AQA = "https://amway-prod.tt.com.pl/bamboo/deploy" +
            "/selectVersionForExecute.action?environmentId=4947970&returnUrl=/deploy/viewAllDeploymentProjects.action";
    private static final String LOGIN = "euvogara";
    private static final String PASSWORD = "Welcome2017";

    private LoginPage loginPage;
    private BuildPage buildPage;
    private static WebDriver driver;

    public RunBuildTest() {
        loginPage = new LoginPage();
        buildPage = new BuildPage();
    }

    @BeforeClass
    public static void startDriver() {
        driver = WebDriverUtil.getDriver();
    }

    @AfterClass
    public static void stopDriver() {
        WebDriverUtil.stopService();
    }

    @Test
    public void runBuildTest() {
        driver.get(BAMBOO_URL);
        loginPage.loginToBamboo(LOGIN, PASSWORD);
        switch(System.getProperty("env")) {
            case "fqa1" :
                driver.get(URL_TO_DEPLOY_FQA1);
                buildPage.selectCreateNewReleaseRadiobutton();
                buildPage.openPlanBranchSelect();
                buildPage.selectDevelopBranch();
                buildPage.setDevelopBuildNumber();
                //buildPage.clickExecuteButton();
                break;
            case "fqa2" :
                driver.get(URL_TO_DEPLOY_FQA2);
                buildPage.selectCreateNewReleaseRadiobutton();
                buildPage.openPlanBranchSelect();
                buildPage.selectLastRelease();
                buildPage.setReleaseVersion();
              //  buildPage.clickExecuteButton();
                break;
            case "aqa" :
                driver.get(URL_TO_DEPLOY_AQA);
                buildPage.selectCreateNewReleaseRadiobutton();
                buildPage.openPlanBranchSelect();
                buildPage.selectLastRelease();
                buildPage.setReleaseVersion();
                //buildPage.clickExecuteButton();
                break;
            default:
                throw new IllegalStateException(System.getProperty("env") + "is not supported environment");
        }
    }

}
