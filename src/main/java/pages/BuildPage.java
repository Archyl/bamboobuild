package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class BuildPage {

    private By createNewReleaseRadiobutton = By.xpath(".//input[@id='releaseTypeCreateOption']");
    private By planBranchSelect = By.xpath(".//div[@id='s2id_newReleaseBranchKey']/a");
    private By searchBranchInput = By.xpath(".//div[@id='select2-drop']/div/input");
    private By searchResultsForBranchInput = By.xpath(".//div[@id='select2-drop']/ul");
    private By artifactNumber = By.xpath(".//span[@class='artifacts-from-result']/a");
    private By releaseVersionInput = By.xpath(".//input[@id='versionName']");
    private By executeButton = By.xpath(".//input[@id='executeManualDeployment_save']");

    public void selectCreateNewReleaseRadiobutton() {
        $(createNewReleaseRadiobutton).click();
    }

    public void openPlanBranchSelect() {
        $(planBranchSelect).click();
    }

    public void selectDevelopBranch() {
        List<SelenideElement> tableOfResults = $(searchResultsForBranchInput).findAll("li");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        for (SelenideElement result : tableOfResults) {
            if (result.getText().contains("develop")) {
                result.click();
                break;
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }

    public void setDevelopBuildNumber() {
        $(releaseVersionInput).setValue("develop-" + getArtifactNumber() + "-monkey");
    }

    public void clickExecuteButton() {
        $(executeButton).click();
    }

    public void selectLastRelease() {
        $(searchBranchInput).setValue("release");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
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
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        $(releaseVersionInput).setValue($(releaseVersionInput).getValue() + "-monkey");
    }

    private String getArtifactNumber() {
        final String artifactNumberValue = $(artifactNumber).text();
        return artifactNumberValue.replace("#", "");
    }

}
