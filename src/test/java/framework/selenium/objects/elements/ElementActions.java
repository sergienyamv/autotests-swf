package framework.selenium.objects.elements;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ElementActions {

    public static ElementAction<Void> click = (element, arguments) -> {
        element.click();
        return null;
    };

    public static ElementAction<Void> clickJs = (element, arguments) -> {
        ((JavascriptExecutor) ((WebDriver) arguments[0])).executeScript("arguments[0].click();", element);
        return null;
    };

    public static ElementAction<Void> scrollTo = (element, arguments) -> {
        ((JavascriptExecutor) ((WebDriver) arguments[0])).executeScript("arguments[0].scrollIntoView(true);", element);
        return null;
    };

    public static ElementAction<Void> type = (element, arguments) -> {
        element.sendKeys((String) arguments[0]);
        return null;
    };

    public static ElementAction<Void> clearAndType = (element, arguments) -> {
        element.clear();
        element.sendKeys((String) arguments[0]);
        return null;
    };

    public static ElementAction<String> getText = (element, arguments) -> element.getText();

    public static ElementAction<String> getAttribute = (element, arguments) -> element.getAttribute(arguments[0].toString());

    public static ElementAction<Void> clickAndType = (element, arguments) -> {
        element.click();
        element.sendKeys((String) arguments[0]);
        return null;
    };

    public static ElementAction<Void> selectByText = (element, arguments) -> {
        Select select = new Select(element);
        select.selectByVisibleText((String) arguments[0]);
        return null;
    };

    public static ElementAction<Void> selectByIndex = (element, arguments) -> {
        Select select = new Select(element);
        select.selectByIndex(Integer.parseInt((String) arguments[0]));
        return null;
    };

    public static ElementAction<WebElement> getFirstSelectedOption = (element, arguments) -> {
        Select select = new Select(element);
        return select.getFirstSelectedOption();
    };

    public static ElementAction<Void> mouseMoveTo = (element, arguments) -> {
        Actions actions = new Actions((WebDriver) arguments[0]);
        actions.moveToElement(element).build().perform();
        return null;
    };

    public static ElementAction<Void> nothing = (element, arguments) -> null;

    public static ElementAction<Boolean> isDisplayed = (element, arguments) -> element.isDisplayed();

    public static ElementAction<WebElement> getWebElement = (element, arguments) -> element;

    public static ElementAction<Void> dragAndDrop = (element, arguments) -> {
        Actions actions = new Actions((WebDriver) arguments[0]);
        actions.dragAndDrop(element, (WebElement) arguments[1]).build().perform();
        return null;
    };

}
