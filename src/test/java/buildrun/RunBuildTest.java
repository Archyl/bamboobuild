package buildrun;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pages.BuildPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

@RunWith(JUnit4.class)
public class RunBuildTest {

    private static final String DRIVER_PATH = System.getProperty("project.basedir")
            + "/src/main/resources/chrome/";
    private static final String BAMBOO_URL = "https://amway-prod.tt.com.pl/bamboo/allPlans.action";
    private static final String URL_TO_DEPLOY_FQA1 = "https://amway-prod.tt.com.pl/bamboo/deploy"
            + "/selectVersionForExecute.action?environmentId=2326531&returnUrl=/deploy"
            + "/viewAllDeploymentProjects.action";
    private static final String URL_TO_DEPLOY_FQA2 = "https://amway-prod.tt.com.pl/bamboo/deploy"
            + "/selectVersionForExecute.action?environmentId=2326533&returnUrl=/deploy"
            + "/viewAllDeploymentProjects.action";
    private static final String URL_TO_DEPLOY_AQA = "https://amway-prod.tt.com.pl/bamboo/deploy" +
            "/selectVersionForExecute.action?environmentId=4947970&returnUrl=/deploy/viewAllDeploymentProjects.action";
    private static final String URL_TO_DEPLOY_TEST = "https://amway-prod.tt.com.pl/bamboo/deploy" +
            "/selectVersionForExecute.action?environmentId=21561355";
    private static final String LOGIN = "euvogara";
    private static final String PASSWORD = "Welcome2018";

    private LoginPage loginPage;
    private BuildPage buildPage;

    public RunBuildTest() {
        loginPage = new LoginPage();
        buildPage = new BuildPage();
    }

    @BeforeClass
    public static void startDriver() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "mac/chromedriver");
        } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "win/chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "linux/chromedriver");
        }
        System.setProperty("selenide.browser", "chrome");
        open(BAMBOO_URL);
    }

    @Test
    public void runBuildTest() {
        loginPage.loginToBamboo(LOGIN, PASSWORD);
        switch(System.getProperty("env")) {
            case "fqa1" :
                open(URL_TO_DEPLOY_FQA1);
                buildPage.selectCreateNewReleaseRadiobutton();
                buildPage.openPlanBranchSelect();
                buildPage.selectDevelopBranch();
                buildPage.setDevelopBuildNumber();
                buildPage.clickExecuteButton();
                break;
            case "fqa2" :
                open(URL_TO_DEPLOY_FQA2);
                buildPage.selectCreateNewReleaseRadiobutton();
                buildPage.openPlanBranchSelect();
                buildPage.selectLastRelease();
                buildPage.setReleaseVersion();
                buildPage.clickExecuteButton();
                break;
            case "aqa" :
                open(URL_TO_DEPLOY_AQA);
                buildPage.selectCreateNewReleaseRadiobutton();
                buildPage.openPlanBranchSelect();
                buildPage.selectLastRelease();
                buildPage.setReleaseVersion();
                buildPage.clickExecuteButton();
                break;
            case "test" :
                open(URL_TO_DEPLOY_TEST);
                buildPage.selectCreateNewReleaseRadiobutton();
                buildPage.openPlanBranchSelect();
                buildPage.selectDevelopBranch();
                buildPage.setDevelopBuildNumber();
                buildPage.clickExecuteButton();
                break;
            default:
                throw new IllegalStateException(System.getProperty("env") + "is not supported environment");
        }
    }

}
