package framework.selenium;

import framework.Logger;
import framework.common.SmartWait;
import framework.common.assertextension.Assert;
import framework.dataproviders.PropertiesResourceManager;
import framework.selenium.objects.elements.Element;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static framework.Logger.getLoc;

/**
 * The main class to access the browser, which extends the capabilities of the standard Webdriver
 */
public final class Browser {
    private static final long SLEEP_SWITCH = 30000L;
    private static final long SLEEP_THREAD_SLEEP = 250;
    private static final long IMPLICITY_WAIT = 10;
    private static final String DEFAULT_CONDITION_TIMEOUT = "defaultConditionTimeout";
    private static final String DEFAULT_PAGE_LOAD_TIMEOUT = "defaultPageLoadTimeout";

    private static final Logger logger = Logger.getInstance();

    static final String PROPERTIES_FILE = "selenium.properties";
    private static final String BROWSER_BY_DEFAULT = "firefox";
    private static final String BROWSER_PROP = "browser";

    private static Browser instance;
    private static ThreadLocal<RemoteWebDriver> driverHolder;
    private static final PropertiesResourceManager props = new PropertiesResourceManager(PROPERTIES_FILE);

    private static String timeoutForPageLoad;
    private static String timeoutForCondition = "6";
    public static final Browsers currentBrowser =
            Browsers.valueOf(System.getProperty(BROWSER_PROP, props.getProperty(BROWSER_PROP, BROWSER_BY_DEFAULT).toUpperCase()));

    /**
     * needed if using two stages
     */
    static boolean useCommonDriver = false;

    /**
     * get RemoteWebDriver
     *
     * @return driver
     */
    static RemoteWebDriver commonDriverHolder = null;

    /**
     * Private constructor (singleton pattern)
     */
    private Browser() {
        Logger.getInstance().info(String.format(getLoc("loc.browser.ready"), currentBrowser.toString()));
    }

    /**
     * Checks is Browser slive
     *
     * @return true\false
     */
    public boolean isBrowserAlive() {
        return driverHolder.get() != null;
    }

    /**
     * Gets instance of Browser
     *
     * @return browser instance
     */
    synchronized public static Browser getInstance() {
        if (instance == null) {
            initProperties();
            instance = new Browser();
        }
        return instance;
    }

    public void exit() {
        try {
            getDriver().quit();
            Logger.getInstance().info(getLoc("loc.browser.driver.qiut"));
        } catch (Exception e) {
            logger.info(this, e);
        } finally {
            driverHolder.set(null);
        }
    }

    /**
     * gets TimeoutForCondition
     *
     * @return timeoutForCondition
     */
    public static String getTimeoutForCondition() {
        return timeoutForCondition;
    }

    /**
     * gets TimeoutForPageLoad
     *
     * @return timeoutForPageLoad
     */
    public String getTimeoutForPageLoad() {
        return timeoutForPageLoad;
    }

    /**
     * init
     */
    private static void initProperties() {
        timeoutForPageLoad = props.getProperty(DEFAULT_PAGE_LOAD_TIMEOUT);
        timeoutForCondition = props.getProperty(DEFAULT_CONDITION_TIMEOUT);
        //init
        driverHolder = new ThreadLocal<RemoteWebDriver>() {
            @Override
            protected RemoteWebDriver initialValue() {
                return Browser.getNewDriver();
            }
        };
    }

    private static RemoteWebDriver getNewDriver() {
        try {
            RemoteWebDriver driver = BrowserFactory.setUp(currentBrowser.toString());
            driver.manage().timeouts().implicitlyWait(IMPLICITY_WAIT, TimeUnit.SECONDS);
            Logger.getInstance().info(getLoc("loc.browser.constructed"));
            return driver;
        } catch (NamingException e) {
            logger.debug("Browser.getNewDriver", e);
        }
        return null;
    }

    /**
     * wait the download page (on Javascript readyState)
     */
    public void waitForPageToLoad() {
        logger.debug("Waiting for page to load");
        ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
            public Boolean apply(final WebDriver d) {
                if (!(d instanceof JavascriptExecutor)) {
                    return true;
                }
                Object result = ((JavascriptExecutor) d)
                        .executeScript("return document['readyState'] ? 'complete' == document.readyState : true");
                return result != null && result instanceof Boolean && (Boolean) result;
            }
        };
        boolean isLoaded = SmartWait.waitForTrue(condition, Long.parseLong(getTimeoutForPageLoad()));
        if (!isLoaded) {
            logger.warn(getLoc("loc.browser.page.timeout"));
        }
    }

    /**
     * waiting, while number of open windows will be more than previous
     *
     * @param prevWndCount - number of previous
     */
    public void waitForNewWindow(final int prevWndCount) {
        ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
            public Boolean apply(final WebDriver d) {
                return d.getWindowHandles().size() > prevWndCount;
            }
        };
        boolean isSuccessWait = SmartWait.waitForTrue(condition);
        if (!isSuccessWait) {
            Assert.assertTrue(false, getLoc("loc.browser.newwindow.notappear"));
        }
    }

    /**
     * Select the previous window (the list of handlers)
     */
    public void selectPreviousWindow() {
        Object[] handles = getDriver().getWindowHandles().toArray();
        String handle = getDriver().getWindowHandle();
        Assert.assertTrue(handles.length > 1, getLoc("loc.browser.windows.count.small"));
        for (int i = 1; i < handles.length; i++) {
            if (handles[i].equals(handle)) {
                getDriver().switchTo().window((String) handles[i - 1]);
                return;
            }
        }
        getDriver().switchTo().window((String) handles[handles.length - 2]);

    }

    /**
     * Select the first window (the list of handlers)
     */
    public void selectFirstWindow() {
        Object[] handles = getDriver().getWindowHandles().toArray();
        getDriver().switchTo().window((String) handles[0]);

    }

    /**
     * We expect the emergence of the new window and select it
     */
    public void selectNewWindow() {
        Object[] handles = getDriver().getWindowHandles().toArray();
        waitForNewWindow(handles.length);
        handles = getDriver().getWindowHandles().toArray();
        getDriver().switchTo().window((String) handles[handles.length - 1]);
    }

    /**
     * Select the last window (the list of handlers)
     */
    public void selectLastWindow() {
        Object[] handles = getDriver().getWindowHandles().toArray();
        getDriver().switchTo().window((String) handles[handles.length - 1]);

    }

    /**
     * maximizes the window
     * <p>
     * works on IE7, IE8, IE9, FF 3.6
     */
    public void windowMaximise() {
        try {
            getDriver().executeScript("if (window.screen) {window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};");
            getDriver().manage().window().maximize();
        } catch (Exception e) {
            logger.debug(e);
            //A lot of browsers crash here
        }
    }

    /**
     * Navgates to the Url
     *
     * @param url Url
     */
    public void navigate(final String url) {
        getDriver().navigate().to(url);
    }

    /**
     * Refresh page.
     */
    public void refresh() {
        getDriver().navigate().refresh();
        Logger.getInstance().info("Page was refreshed.");
    }

    public void back() {
        getDriver().navigate().back();
        Logger.getInstance().info("Return to previous page");
    }

    public static RemoteWebDriver getDriver() {
        if (useCommonDriver && commonDriverHolder != null) {
            return commonDriverHolder;
        }
        if (driverHolder.get() == null) {
            driverHolder.set(getNewDriver());
        }
        commonDriverHolder = driverHolder.get();
        return commonDriverHolder;
    }

    /**
     * Open new window by hot keys Ctrl + N, webdriver switch focus on it.
     */
    public void openNewWindow() {
        getDriver().findElement(By.xpath("//body")).sendKeys(Keys.CONTROL, "n");
        Object[] headers = getDriver().getWindowHandles().toArray();
        getDriver().switchTo().window(headers[headers.length - 1].toString());
    }

    /**
     * click and switch to new window. (works on IE also)
     *
     * @param element element
     */
    public void clickAndSwitchToNewWindow(final Element element) {
        Set<String> existingHandles = getDriver().getWindowHandles();
        element.click();

        String foundHandle = null;
        long endTime = System.currentTimeMillis() + SLEEP_SWITCH;
        while (foundHandle == null && System.currentTimeMillis() < endTime) {
            Set<String> currentHandles = getDriver().getWindowHandles();
            foundHandle = getNewWindowHandle(existingHandles, currentHandles);
            if (foundHandle == null) {
                try {
                    Thread.sleep(SLEEP_THREAD_SLEEP);
                } catch (InterruptedException e) {
                    logger.debug(this, e);
                    logger.fatal("new window not found");
                }
            }
        }

        if (foundHandle != null) {
            getDriver().switchTo().window(foundHandle);
        } else {
            logger.fatal("new window not found");
        }
    }

    public String getNewWindowHandle(Set<String> previousHandles, Set<String> currentHandles) {
        if (currentHandles.size() != previousHandles.size()) {
            for (String currentHandle : currentHandles) {
                if (!previousHandles.contains(currentHandle)) {
                    logger.info("new window was found");
                    return currentHandle;
                }
            }
        }
        return null;
    }

    /**
     * Trigger
     *
     * @param script  script
     * @param element element
     */
    public void trigger(final String script, final WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript(script, element);
    }

    /**
     * Executes a script
     *
     * @param script The script to execute
     * @return Object
     * @note Really should only be used when the web driver is sucking at exposing functionality natively
     */
    public Object trigger(final String script) {
        return ((JavascriptExecutor) getDriver()).executeScript(script);
    }

    /**
     * Opens a new tab for the given URL (doesn't work on IE)
     *
     * @param url The URL to
     */
    public void openTab(final String url) {
        String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
        Object element = trigger(String.format(script, url));
        if (element instanceof WebElement) {
            WebElement anchor = (WebElement) element;
            anchor.click();
            trigger("var a=arguments[0];a.parentNode.removeChild(a);", anchor);
        } else {
            try {
                throw new Exception("Unable to open tab with JavaScript");
            } catch (Exception e) {
                Logger.getInstance().debug(e.getMessage());
            }
        }
    }

    /**
     * Gets current URL
     *
     * @return current URL
     */
    public String getLocation() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Executes Java Scipt
     *
     * @param script Java Script
     */
    public void jsExecute(final String script) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(script);
    }

    public String makeScreen(String fileName) {
        String pageSourcePath = String.format("target" + File.separator +
                File.separator + "screenshots/%1$s.txt", fileName);
        String screenshotPath = String.format("target" + File.separator +
                File.separator + "screenshots/%1$s.png", fileName);
        try {
            String pageSource = getDriver().getPageSource();
            FileUtils.writeStringToFile(new File(pageSourcePath), pageSource);
        } catch (Exception e1) {
            logger.debug(this, e1);
        }
        try {
            File screen = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            File addedNewFile = new File(screenshotPath);
            FileUtils.copyFile(screen, addedNewFile);
        } catch (Exception e) {
            logger.debug(this, e);
        }
        return new File(screenshotPath).getAbsolutePath();
    }

    /**
     * Browsers enumeration
     */
    public enum Browsers {
        /**
         * @uml.property name="fIREFOX"
         * @uml.associationEnd
         */
        FIREFOX("firefox"),
        /**
         * @uml.property name="iEXPLORE"
         * @uml.associationEnd
         */
        IEXPLORE("iexplore"),
        /**
         * @uml.property name="cHROME"
         * @uml.associationEnd
         */
        CHROME("chrome"),
        /**
         * @uml.property name="oPERA"
         * @uml.associationEnd
         */
        OPERA("opera"),
        /**
         * @uml.property name="sAFARI"
         * @uml.associationEnd
         */
        SAFARI("safari"),
        /**
         * @uml.property name="aNDROID"
         * @uml.associationEnd
         */
        SELENDROID("selendroid"),
        /**
         * @uml.property name="Appium_Android"
         * @uml.associationEnd
         */
        APPIUM_ANDROID("appium_android"),
        /**
         * @uml.property name="IOS"
         * @uml.associationEnd
         */
        IOS("ios");

        private String value;

        /**
         * Constructor
         *
         * @param values Value
         */
        Browsers(final String values) {
            value = values;
        }

        /**
         * Returns string value
         *
         * @return String value
         */
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Set cookies into a webdriver instanse.
     *
     * @param cookies list
     */
    public void setCookies(Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            getDriver().manage().addCookie(cookie);
        }
    }

    /**
     * does switch to window with the specified handle and then try to set cookies into it
     *
     * @param handle target window handle.
     * @return true if operation was completed successfully, false if not
     */
    public boolean switchToWindowAndSetCookies(String handle) {
        Set<Cookie> cookies = getDriver().manage().getCookies();
        try {
            for (String winHandle : getDriver().getWindowHandles()) {
                getDriver().switchTo().window(winHandle);
                setCookies(cookies);
            }
        } catch (Exception e) {
            Logger.getInstance().info("There was an error during switch and set cookies.\r\n" + e.getMessage());
            getDriver().close();
            getDriver().switchTo().window(handle);
            return false;
        }
        return true;
    }

    public void switchToFrame(String frameId) {
        Logger.getInstance().debug(String.format("Switch to :: frame %s", frameId));
        getDriver().switchTo().frame(frameId);
    }

    public void switchToDefaultContent() {
        Logger.getInstance().debug("Switch to :: page content");
        getDriver().switchTo().defaultContent();
    }

    public static void getExplicitWait(final By param) throws IOException {
        WebElement someElement = (new WebDriverWait(getDriver(), Long.valueOf(timeoutForCondition)))
                .until(new ExpectedCondition<WebElement>() {
                    @Override
                    public WebElement apply(WebDriver d) {
                        return d.findElement(param);
                    }
                });
    }

    public static void useCommonDriver(boolean use) {
        useCommonDriver = use;
    }

    public static void setTimeoutForCondition(String timeoutForCondition) {
        Browser.timeoutForCondition = timeoutForCondition;
    }

    public static int getDefaultSriptsTimeout() {
        PropertiesResourceManager prop = new PropertiesResourceManager(PROPERTIES_FILE);
        return Integer.parseInt(prop.getProperty("defaultScriptsLoad"));
    }

    public static long waitCondition() {
        return 5;
    }
}
