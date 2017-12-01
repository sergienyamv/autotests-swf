package framework.selenium.objects.elements;


import java.util.List;

import org.openqa.selenium.*;

public class TextBox extends Element {
    public TextBox(List<By> selectors, String name) {
        super(selectors, name);
    }

    public TextBox(By selector, String name) {
        super(selector, name);
    }

    public TextBox(By selector, ElementState elementState, String name) {
        super(selector, elementState, name);
    }
}