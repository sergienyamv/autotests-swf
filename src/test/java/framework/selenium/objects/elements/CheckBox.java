package framework.selenium.objects.elements;

import framework.Logger;
import org.openqa.selenium.By;

import java.util.List;

public class CheckBox extends Element {
    public CheckBox(List<By> selectors, String name) {
        super(selectors, name);
    }

    public CheckBox(By selector, String name) {
        super(selector, name);
    }

    public CheckBox(By selector, ElementState elementState, String name) {
        super(selector, elementState, name);
    }

    public void setState(boolean state) {
        Logger.getInstance().info(String.format("Element : %s change state : %s", getName(), state));
        if (state != isChecked()) {
            clickJs();
        }
    }

    public boolean isChecked() {
        return getWebElement().isSelected();
    }
}