package vk.pages;

import framework.selenium.objects.BasePage;
import framework.selenium.objects.elements.Button;
import framework.selenium.objects.elements.TextBox;
import org.openqa.selenium.By;
import vk.models.NamePasswordModel;

import java.util.Optional;

public class LoginPage extends BasePage {
    private static By uniqueLocator = By.xpath("//div[@id='wrapper']/div[@id='content']//input[@id='username']");

    public LoginPage() {
        super(uniqueLocator);
    }

    private Button getSubmitButton() {
        return new Button(By.xpath("//input[@type='submit']"), "Submit login");
    }

    private TextBox getUsernameTextBox() {
        return new TextBox(By.id("username"), "Username input");
    }

    private TextBox getPasswordTextBox() {
        return new TextBox(By.id("password"), "Username input");
    }

    public void loginToSystem(NamePasswordModel namePasswordModel) {
        Optional<NamePasswordModel> optional = Optional.ofNullable(namePasswordModel);
        optional.map(NamePasswordModel::getUserName).ifPresent(getUsernameTextBox()::clearAndType);
        optional.map(NamePasswordModel::getPassword).ifPresent(getPasswordTextBox()::clearAndType);
        getSubmitButton().click();
    }
}
