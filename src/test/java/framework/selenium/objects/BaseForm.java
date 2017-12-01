package framework.selenium.objects;

import framework.selenium.objects.elements.Element;
import org.openqa.selenium.By;

public abstract class BaseForm {
    private By formLocator;
    private String formName;

    public BaseForm(By formLocator, String formName) {
        this.formName = formName;
        this.formLocator = formLocator;
    }

    public boolean isDisplayedAndEnabled() {
        Element formLocatorElement = new Element(formLocator, formName);
        return formLocatorElement.isDisplayedAndEnabled();
    }

    public boolean isEditableStateView() {
        return false;
    }
}