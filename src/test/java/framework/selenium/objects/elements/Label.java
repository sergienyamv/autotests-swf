package framework.selenium.objects.elements;

import java.util.List;

import org.openqa.selenium.*;

public class Label extends Element {
    public Label(List<By> selectors, String name) {
        super(selectors, name);
    }

    public Label(By selector, String name) {
        super(selector, name);
    }

    public Label(By selector, ElementState elementState, String name) {
        super(selector, elementState, name);
    }
}