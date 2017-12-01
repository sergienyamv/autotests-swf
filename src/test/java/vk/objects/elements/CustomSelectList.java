package vk.objects.elements;

import framework.common.SmartWait;
import framework.selenium.objects.elements.Element;
import framework.selenium.objects.elements.SelectList;
import org.openqa.selenium.By;

public class CustomSelectList extends SelectList {
    public CustomSelectList(By selector, String name) {
        super(selector, name);
    }

    @Override
    public void selectValue(String value) {
        clearAndType(value + '\n');
    }

    @Override
    public void selectValueAndWait(String value) {
        selectValue(value);
        SmartWait.waitForTrue(item -> false, 1);
    }

    public void clickAndSelectValue(String value) {
        click();
        new Element(By.xpath(String.format("//li[contains(@class,'active-result') and contains(.,'%s')]", value)), value + " element").click();
        SmartWait.waitForTrue(item -> false, 1);
    }
}
