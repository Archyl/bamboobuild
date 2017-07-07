package pages;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private By loginInput = By.xpath(".//input[@id='loginForm_os_username']");
    private By passwordInput = By.xpath(".//input[@id='loginForm_os_password']");
    private By loginButton = By.xpath(".//input[@id='loginForm_save']");

    public void loginToBamboo(final String login, final String password) {
        $(loginInput).setValue(login);
        $(passwordInput).setValue(password);
        $(loginButton).click();
    }

}
