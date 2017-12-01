package framework.selenium.objects.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import framework.common.SmartWait;
import framework.dataproviders.ProjectFilesProvider;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import framework.common.assertextension.Assert;
import framework.selenium.Browser;
import framework.Logger;
import javax.annotation.Nullable;

public class Element {
    private By cashedSelector;
    private List<By> selectors;
    private String name;
    private final ElementState elementState;

    public Element(List<By> selectors, String name) {
        this.selectors = selectors;
        this.name = name;
        this.elementState = ElementStates.displayedAndEnabled;
    }

    public Element(By selector, String name) {
        this.selectors = new ArrayList<>(Arrays.asList(selector));
        this.name = name;
        this.elementState = ElementStates.displayedAndEnabled;
    }

    public Element(By selector, ElementState elementState, String name) {
        this.selectors = new ArrayList<>(Arrays.asList(selector));
        this.name = name;
        this.elementState = elementState;
    }

    public String getName() {
        return name;
    }

    public <T> T waitElement(ElementState state, ElementAction<T> action, Object[] args) {
        return waitElement(state, action, args, Long.parseLong(Browser.getTimeoutForCondition()));
    }

    public static void waitForElementCountIsNotChanged(By selector, String elName) {
        waitForElementCountIsNotChanged(selector, ElementStates.displayedAndEnabled, elName, Long.parseLong(Browser.getTimeoutForCondition()));
    }

    private String getSelectorsString() {
        StringBuilder sb = new StringBuilder();
        for (By selector: selectors) {
            sb.append(selector.toString());
        }
        return sb.toString().replaceAll("\n", " | ");
    }

    public static void waitForElementCountIsNotChanged(final By selector, ElementState state, String elName, long timeout) {
        Browser.getInstance().waitForPageToLoad();
        FluentWait fluentWait = (new FluentWait(Browser.getDriver())).withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(1l, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
        final List<Element> elements = new ArrayList();
        final int[] size = new int[1];
        size[0] = 0;
        fluentWait.until(new ExpectedCondition<Boolean>() {
            @Nullable
            public Boolean apply(@Nullable WebDriver driver) {
                elements.clear();
                driver.manage().timeouts().implicitlyWait(0L, TimeUnit.SECONDS);
                int newSize = driver.findElements(selector).size();
                Logger.getInstance().debug(String.format("size:%1$s;new size:%2$s", size[0], newSize));
                if (size[0] != newSize) {
                    size[0] = newSize;
                    return false;
                } else {
                    return size[0] == newSize;
                }
            }
        });
    }


    public <T> T waitElement(ElementState state, ElementAction<T> action, Object[] args, Long timeout) {
        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(Browser.getDriver())
                .withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(StaleElementReferenceException.class);
        ArrayList<T> buf = new ArrayList<>();
        try {
            fluentWait.until((WebDriver driver) -> {
                buf.clear();
                List<By> selectorsList = new ArrayList<>();
                if (cashedSelector != null) {
                    selectorsList.add(cashedSelector);
                } else {
                    selectorsList = selectors;
                }
                for (By selector : selectors) {
                    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                    List<WebElement> foundElements = driver.findElements(selector);
                    Logger.getInstance().debug(String.format("Found %1$s elements by locator %2$s", foundElements.size(),
                            selectors.get(0)));

                    if (foundElements.size() == 0 && state.equals(ElementStates.notExists)) {
                        buf.add(null);
                        return true;
                    }

                    if (foundElements.size() >= 1) {

                        if (state.equals(ElementStates.notDisplayed) && !foundElements.get(0).isDisplayed()) {
                            Logger.getInstance().debug("First will of the found elements be used");
                            buf.add(null);
                            return true;
                        }

                        cashedSelector = selector;
                        for (WebElement el : foundElements) {
                            if (state != null && state.inState(el)) {
                                buf.add(action.doAction(el, args));
                                return true;
                            }
                        }
                    }
                }
                return false;
            });
        } catch (TimeoutException exp) {
            Browser.getInstance().makeScreen(name + "_" + System.currentTimeMillis());
            Logger.getInstance().fatal(String.format("Element %1$s was not found in %2$s seconds :: %3$s", name, Browser.getTimeoutForCondition(), getSelectorsString()));
        }
        if (buf.size() < 1) {
            return null;
        }
        return buf.get(0);
    }

    public static List<Element> waitAndGetElements(By selector, ElementState state, long timeout,
                                                   String elName, int expectedElNumber) {
        Browser.getInstance().waitForPageToLoad();
        waitForElementCountIsNotChanged(selector, state, elName, timeout);
        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(Browser.getDriver())
                .withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(StaleElementReferenceException.class);
        List<Element> elements = new ArrayList<>();

        fluentWait.until((WebDriver driver) -> {
            elements.clear();
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            List<WebElement> foundElements = driver.findElements(selector);
            Logger.getInstance()
                    .debug(String.format("Found %1$s elements by locator %2$s", foundElements.size(), selector));
            for (WebElement el : foundElements) {
                if (state.inState(el)) {
                    String xpath = getAbsoluteXPath(el, driver);
                    elements.add(new Element(By.xpath(xpath), elName));
                }
            }
            if (elements.size() >= expectedElNumber) {
                return true;
            }
            return false;
        });
        return elements;
    }

    private static String getAbsoluteXPath(WebElement element, WebDriver driver) {
        return (String) ((JavascriptExecutor) Browser.getDriver()).executeScript(ProjectFilesProvider.getJavaScript("getAbsoluteXPath"), element);
    }

    public static String getAbsoluteXPath(Element element, WebDriver driver) {
        return (String) ((JavascriptExecutor) Browser.getDriver()).executeScript(ProjectFilesProvider.getJavaScript("getAbsoluteXPath"), element.getWebElement());
    }

    public String getAbsoluteXPath() {
        return (String) Browser.getDriver().executeScript(ProjectFilesProvider.getJavaScript("getAbsoluteXPath"), getWebElement());
    }

    public static List<Element> waitAndGetElements(By selector, String elName) {
        return waitAndGetElements(selector, ElementStates.displayedAndEnabled,
                Long.parseLong(Browser.getTimeoutForCondition()), elName, 1);
    }

    public Boolean isElementInState(ElementState expectedState) {
        return isElementInState(expectedState, Long.parseLong(Browser.getTimeoutForCondition()));
    }

    public Boolean isElementInState(ElementState expectedState, Long timeout) {
        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(Browser.getDriver())
                .withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(StaleElementReferenceException.class);
        ArrayList<Boolean> buf = new ArrayList<>();
        try {
            fluentWait.until(driver -> {
                buf.clear();
                driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                List<WebElement> foundElements = driver.findElements(selectors.get(0));
                if (foundElements.size() == 0 && expectedState.equals(ElementStates.notExists)) {
                    buf.add(true);
                    return true;
                }
                if (foundElements.size() == 1 && expectedState.equals(ElementStates.exists)) {
                    buf.add(true);
                    return true;
                }
                if (foundElements.size() >= 1 && (expectedState.equals(ElementStates.displayedAndEnabled)
                        || expectedState.equals(ElementStates.exists))) {
                    Logger.getInstance().debug(String.format("Found %1$s elements by locator %2$s",
                            foundElements.size(), selectors.get(0)));
                    for (WebElement el : foundElements) {
                        Boolean state = el.isDisplayed();
                        buf.add(state);
                        return state;
                    }
                }
                return false;
            });
        } catch (TimeoutException exp) {
            Logger.getInstance().debug(exp.getMessage());
        }
        if (buf.size() < 1) {
            return false;
        }
        return buf.get(0);
    }

    public void type(String text) {
        Logger.getInstance().info(String.format("Type text '%1$s' into field %2$s", text, name));
        waitElement(elementState, ElementActions.type, new String[]{text});
    }

    public void clearAndType(String text) {
        Logger.getInstance().info(String.format("Clear and type text '%1$s' into field %2$s", text, name));
        waitElement(elementState, ElementActions.clearAndType, new String[]{text});
    }

    public void selectByText(String value) {
        Logger.getInstance().info(String.format("Select text '%1$s' in field %2$s", value, name));
        waitElement(elementState, ElementActions.selectByText, new String[]{value});
    }

    public void click() {
        Logger.getInstance().info("Click :: " + name);
        waitElement(elementState, ElementActions.click, new String[]{""});
    }

    public void click(int times) {
        if (times < 1) {
            Logger.getInstance().info(String.format("Click %s times :: not possible", times));
            return;
        }
        Logger.getInstance().info(String.format("Click %s times :: %s", times, name));
        for (int i = 0; i < times; i++) {
            waitElement(elementState, ElementActions.click, new String[]{""});
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickJs() {
        Logger.getInstance().info("Click :: " + name);
        waitElement(elementState, ElementActions.clickJs, new Object[]{Browser.getDriver()});
    }

    public void scrollToView() {
        waitElement(elementState, ElementActions.scrollTo, new Object[]{Browser.getDriver()});
    }

    public void clickAndSwitchToNewWindow() {
        Logger.getInstance().info("Click and switch to new window :: " + name);
        Set<String> previousHandles = Browser.getDriver().getWindowHandles();
        waitElement(elementState, ElementActions.click, new String[]{""});
        Browser.getInstance().waitForNewWindow(previousHandles.size());
        Set<String> currentHandles = Browser.getDriver().getWindowHandles();
        String handle = Browser.getInstance().getNewWindowHandle(previousHandles, currentHandles);
        Browser.getInstance().switchToWindowAndSetCookies(handle);
    }

    public void mouseMoveTo() {
        Logger.getInstance().info("Move to :: " + name);
        waitElement(elementState, ElementActions.mouseMoveTo, new WebDriver[]{Browser.getDriver()});
    }

    public void waitForIsNotExists() {
        Logger.getInstance().info("Wait for is not exists :: " + name);
        waitElement(ElementStates.notExists, ElementActions.nothing, new WebDriver[]{Browser.getDriver()});
    }

    public void waitForIsNotDisplayed() {
        Logger.getInstance().info("Wait for is not displayed :: " + name);
        waitElement(ElementStates.notDisplayed, ElementActions.nothing, new WebDriver[]{Browser.getDriver()});
    }

    public boolean isDisplayedAndEnabled() {
        return isDisplayedAndEnabled(5L);
    }

    public boolean isDisplayedAndEnabled(Long waitInSeconds) {
        Logger.getInstance().info(String.format("Checking element with unique locator: %s", selectors));
        return isElementInState(ElementStates.displayedAndEnabled, waitInSeconds);
    }

    public boolean exists() {
        return isElementInState(ElementStates.exists, 20l);
    }

    public String getText() {
        return (String) waitElement(elementState, ElementActions.getText, new Object[]{});
    }

    public String getAttribute(String attr) {
        return (String) waitElement(elementState, ElementActions.getAttribute, new String[]{attr});
    }

    public void waitForIsDisplayed() {
        waitElement(ElementStates.displayedAndEnabled, ElementActions.nothing, new Object[]{});
    }

    protected WebElement getWebElement() {
        return (WebElement) this.waitElement(this.elementState, ElementActions.getWebElement, new Object[0]);
    }

    public void dragAndDrop(Element target) {
        Logger.getInstance().info("Drag element " + this.name + " to element " + target.getName());
        this.waitElement(ElementStates.displayedAndEnabled, ElementActions.dragAndDrop, new Object[]{Browser.getDriver(), target.getWebElement()});
    }

    public boolean isAttributeContains(String attr, String value) {
        Logger.getInstance().info("Check attribute :: " + name + " [" + attr + "=" + value + "]");
        String str = waitElement(ElementStates.exists, ElementActions.getAttribute, new String[]{attr});
        return str.contains(value);
    }

    public String getAttributeOfFirstElement( String attr) {
        return (String) waitElement(ElementStates.exists, ElementActions.getAttribute, new String[]{attr});
    }
}