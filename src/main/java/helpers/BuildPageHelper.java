package helpers;

import pages.BuildPage;
import utils.properties.PropertiesService;

import static utils.properties.PropertiesConstants.DEPLOY_TYPE;

public class BuildPageHelper {

    private BuildPage buildPage;

    public BuildPageHelper() {
        buildPage = new BuildPage();
    }

    public void startDeploy() {
        switch (PropertiesService.getProperty(DEPLOY_TYPE)) {
            case "develop" :
                startDeployFromDevelopBranch();
                if (buildPage.isErrorShown()) {
                    startDevelopDeployFromExistingBranch();
                }
                break;
            case "release" :
                startDeployFromReleaseBranch();
                if (buildPage.isErrorShown()) {
                    startReleaseDeployFromExistingBranch();
                }
                break;
            default:
                throw new IllegalArgumentException(PropertiesService.getProperty(DEPLOY_TYPE) + " deploy type is " +
                        "not supported");
        }
    }

    private void startDeployFromDevelopBranch() {
        buildPage.selectCreateNewReleaseRadiobutton();
        buildPage.openPlanBranchSelect();
        buildPage.selectDevelopBranch();
        buildPage.setDevelopBuildNumber();
        buildPage.clickExecuteButton();
    }

    private void startDeployFromReleaseBranch() {
        buildPage.selectCreateNewReleaseRadiobutton();
        buildPage.openPlanBranchSelect();
        buildPage.selectLastRelease();
        buildPage.setReleaseVersion();
        buildPage.clickExecuteButton();
    }

    private void startDevelopDeployFromExistingBranch() {
        buildPage.selectDeployExistingBranchOption();
        buildPage.openExistingPlanBranchSelect();
        buildPage.selectDevelopBranch();
        buildPage.setExistingDevelop();
        buildPage.clickExecuteButton();
    }

    private void startReleaseDeployFromExistingBranch() {
        buildPage.selectDeployExistingBranchOption();
        buildPage.openExistingPlanBranchSelect();
        buildPage.selectLastRelease();
        buildPage.setExistingRelease();
        buildPage.clickExecuteButton();
    }
}
