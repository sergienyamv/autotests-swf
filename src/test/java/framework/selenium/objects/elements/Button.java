package framework.selenium.objects.elements;

import java.util.List;

import org.openqa.selenium.*;

public class Button extends Element {
    public Button(List<By> selectors, String name) {
        super(selectors, name);
    }

    public Button(By selector, String name) {
        super(selector, name);
    }

    public Button(By selector, ElementState elementState, String name) {
        super(selector, elementState, name);
    }
}