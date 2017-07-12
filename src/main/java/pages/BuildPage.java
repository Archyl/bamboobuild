package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.timeouts.TimeoutUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class BuildPage {

    private static final Logger LOG = LoggerFactory.getLogger(BuildPage.class);
    private static String releaseNumber = "";
    private static String developNumber = "";
    private By createNewReleaseRadiobutton = By.xpath(".//input[@id='releaseTypeCreateOption']");
    private By planBranchSelect = By.xpath(".//div[@id='s2id_newReleaseBranchKey']/a");
    private By searchBranchInput = By.xpath(".//div[@id='select2-drop']/div/input");
    private By searchResultsForBranchInput = By.xpath(".//div[@id='select2-drop']/ul");
    private By artifactNumber = By.xpath(".//span[@class='artifacts-from-result']/a");
    private By releaseVersionInput = By.xpath(".//input[@id='versionName']");
    private By executeButton = By.xpath(".//input[@id='executeManualDeployment_save']");
    private By deployExistingBranchRadiobutton = By.xpath(".//input[@id='releaseTypePromoteOption']");
    private By errorMessage = By.xpath(".//div[@id='fieldArea_versionName']/div");
    private By existingPlanBranchSelect = By.xpath(".//div[@id='s2id_promoteReleaseBranchKey']/a");
    private By existingReleaseSelect = By.xpath(".//div[@id='s2id_promoteVersion']/a");
    private By existingReleaseInput = By.xpath(".//div[@id='select2-drop']/div/input");

    public void selectCreateNewReleaseRadiobutton() {
        $(createNewReleaseRadiobutton).click();
    }

    public void openPlanBranchSelect() {
        TimeoutUtil.delay(3000);
        $(planBranchSelect).click();
    }

    public void selectDevelopBranch() {
        List<SelenideElement> tableOfResults = $(searchResultsForBranchInput).findAll("li");
        TimeoutUtil.delay(5000);
        for (SelenideElement result : tableOfResults) {
            if (result.getText().contains("develop")) {
                result.click();
                break;
            }
        }
        TimeoutUtil.delay(5000);
    }

    public void setDevelopBuildNumber() {
        $(releaseVersionInput).setValue("develop-" + getArtifactNumber() + "-monkey");
        developNumber = $(releaseVersionInput).getValue();
        LOG.info("build version = " + developNumber);
    }

    public void setExistingDevelop() {
        TimeoutUtil.delay(5000);
        $(existingReleaseSelect).click();
        $(existingReleaseInput).setValue(developNumber);
        TimeoutUtil.delay(2000);
        List<SelenideElement> tableOfResults = $(searchResultsForBranchInput).findAll("li");
        TimeoutUtil.delay(5000);
        for (SelenideElement result : tableOfResults) {
            if (result.getText().contains(developNumber)) {
                result.click();
                break;
            }
        }
        TimeoutUtil.delay(5000);
    }

    public void clickExecuteButton() {
        $(executeButton).click();
        TimeoutUtil.delay(5000);
    }

    public void selectLastRelease() {
        $(searchBranchInput).setValue("release");
        TimeoutUtil.delay(5000);
        List<SelenideElement> releases = $(searchResultsForBranchInput).findAll("li");
        List<String> releaseNumbers = new ArrayList<>();
        for (SelenideElement release : releases) {
            String releaseNumber = release.text();
            String[] numbers = releaseNumber.split("_");
            releaseNumbers.add(numbers[0]);
        }
        Collections.sort(releaseNumbers);
        for (SelenideElement release : releases) {
            if (release.getText().contains(releaseNumbers.get(releaseNumbers.size()-1))) {
                release.click();
                break;
            }
        }
    }

    public void setReleaseVersion() {
        TimeoutUtil.delay(5000);
        $(releaseVersionInput).setValue($(releaseVersionInput).getValue() + "-monkey");
        releaseNumber = $(releaseVersionInput).getValue();
        LOG.info("release version to build = " + $(releaseVersionInput).getValue());
    }

    public void setExistingRelease() {
        TimeoutUtil.delay(5000);
        $(existingReleaseSelect).click();
        $(existingReleaseInput).setValue(releaseNumber);
        TimeoutUtil.delay(2000);
        List<SelenideElement> tableOfResults = $(searchResultsForBranchInput).findAll("li");
        TimeoutUtil.delay(5000);
        for (SelenideElement result : tableOfResults) {
            if (result.getText().contains(releaseNumber)) {
                result.click();
                break;
            }
        }
        TimeoutUtil.delay(5000);
    }

    public void selectDeployExistingBranchOption() {
        $(deployExistingBranchRadiobutton).click();
    }

    public boolean isErrorShown() {
        return $(errorMessage).isDisplayed();
    }

    public void openExistingPlanBranchSelect() {
        TimeoutUtil.delay(3000);
        $(existingPlanBranchSelect).click();
    }

    private String getArtifactNumber() {
        TimeoutUtil.delay(3000);
        final String artifactNumberValue = $(artifactNumber).text();
        return artifactNumberValue.replace("#", "");
    }

}
