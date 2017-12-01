package framework.selenium.objects.menu;

import framework.selenium.objects.elements.Element;
import framework.selenium.objects.elements.ElementState;
import org.openqa.selenium.By;

import java.util.List;

public class MenuItem extends Element {
    public MenuItem(List<By> selectors, String name) {
        super(selectors, name);
    }

    public MenuItem(By selector, String name) {
        super(selector, name);
    }

    public MenuItem(By selector, ElementState elementState, String name) {
        super(selector, elementState, name);
    }
}