package framework.selenium.objects.elements;

public class ElementStates {
    public static ElementState exists = element -> element != null;

    public static ElementState notExists = element -> true;

    public static ElementState displayedAndEnabled = element -> element.isDisplayed() && element.isEnabled();

    public static ElementState displayed = element -> element.isDisplayed();

    public static ElementState notDisplayed = element -> !element.isDisplayed();
}