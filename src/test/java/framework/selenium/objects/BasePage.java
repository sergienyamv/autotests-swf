package framework.selenium.objects;

import framework.Logger;
import framework.common.assertextension.Assert;
import framework.selenium.Browser;
import framework.selenium.objects.elements.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    private static final int DEFAULT_TIMEOUT_LOAD_SCRIPT = Browser.getDefaultSriptsTimeout();
    public By uniqueLocator;

    public BasePage(By uniqueLocator) {
        waitForLoadScripts();
        this.uniqueLocator = uniqueLocator;
        if (!isPageOpened()) {
            Logger.getInstance().error("cannot create page, unique locator not found");
            Assert.fail("cannot create page, unique elements not found");
            throw new IllegalStateException("unique elements not found");
        }
    }

    public boolean isPageOpened() {
        return isPageOpened(uniqueLocator);
    }

    public static boolean isPageOpened(By uniqueLocator) {
        Logger.getInstance().info(String.format("Checking page with unique locator: %s", uniqueLocator));
        Element element = new Element(uniqueLocator, String.format("Page with unique locator: %s", uniqueLocator));
        return element.exists();
    }

    public static void waitForLoadScripts() {
        waitForLoadScripts(DEFAULT_TIMEOUT_LOAD_SCRIPT);
    }

    public static void waitForLoadScripts(int timeoutInSeconds) {
        Logger.getInstance().info("wait load scripts");
        new WebDriverWait(Browser.getDriver(), timeoutInSeconds).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}