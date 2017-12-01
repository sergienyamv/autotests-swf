package framework.selenium.objects.elements;

import org.openqa.selenium.WebElement;

public interface ElementAction<T> {
    T doAction(WebElement element, Object[] arguments);
}