package framework.selenium.objects.elements;

import framework.Logger;
import framework.common.SmartWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SelectList extends Element {

    public SelectList(By selector, String name) {
        super(selector, name);
    }

    public void selectValue(String value) {
        Logger.getInstance().info(String.format("Select value '%1$s' into field %2$s", value, this.getName()));
        waitElement(ElementStates.displayedAndEnabled, ElementActions.selectByText, new String[]{value});
    }

    public void selectValueAndWait(String value) {
        Logger.getInstance().info(String.format("Select value '%1$s' into field %2$s", value, this.getName()));
        waitElement(ElementStates.displayedAndEnabled, ElementActions.selectByText, new String[]{value});
        SmartWait.waitForTrue(item -> false, 1);
    }

    public WebElement getFirstSelectedOption() {
        return waitElement(ElementStates.displayedAndEnabled, ElementActions.getFirstSelectedOption, new Object[0]);
    }
}