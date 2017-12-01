package framework.selenium;

import framework.dataproviders.ProjectFilesProvider;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import framework.selenium.Browser.Browsers;
import framework.Logger;

import javax.naming.NamingException;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * The class-initializer-based browser string parameter.
 */
final public class BrowserFactory {
    private static final String LOCALHOST = "localhost";
    private static final Logger logger = Logger.getInstance();

    /**
     * Setting up Driver
     *
     * @param type Browser type
     * @return RemoteWebDriver
     */
    public static RemoteWebDriver setUp(final Browsers type) {
        Proxy proxy = null;
        return getWebDriver(type, proxy);
    }

    /**
     * Setting up Driver
     *
     * @param type Browser type
     * @return RemoteWebDriver
     * @throws NamingException NamingException
     */
    public static RemoteWebDriver setUp(final String type) throws NamingException {
        for (Browsers t : Browsers.values()) {
            if (t.toString().equalsIgnoreCase(type)) {
                return setUp(t);
            }
        }
        throw new NamingException(logger.getLoc("loc.browser.name.wrong") + ":\nchrome\nfirefox\niexplore\nopera\nsafari");
    }

    //////////////////
    // Private methods
    //////////////////

    private static RemoteWebDriver getWebDriver(final Browsers type, Proxy proxy) {
        DesiredCapabilities capabilitiesProxy = new DesiredCapabilities();
        if (proxy != null) {
            capabilitiesProxy.setCapability(CapabilityType.PROXY, proxy);
        }
        switch (type) {
            case CHROME:
                return getChromeDriver(proxy);
            case FIREFOX:
                return getFirefoxDriver(capabilitiesProxy);
            case IEXPLORE:
                return getIEDriver(proxy);
            case SAFARI:
                return getSafariDriver(capabilitiesProxy);
            default:
                return null;
        }
    }

    private static RemoteWebDriver getFirefoxDriver(DesiredCapabilities capabilities) {
        String platform = System.getProperty("os.name").toLowerCase();
        String fileName = "";
        if (platform.contains("win")) {
            fileName = "geckodriver.exe";
        } else if (!platform.contains("mac") && !platform.contains("linux")) {
            logger.fatal(String.format("Unsupported platform: %1$s for chrome browser %n", new Object[]{platform}));
        } else {
            fileName = "geckodriver";
        }
        File caps = new File(ProjectFilesProvider.getFileAbsolutePath("/src/test/resources/" + fileName));
        System.setProperty("webdriver.gecko.driver", caps.getAbsolutePath());
        return new FirefoxDriver();
    }

    private static RemoteWebDriver getChromeDriver(Proxy proxy) {
        String platform = System.getProperty("os.name").toLowerCase();
        String fileName = "";
        if (platform.contains("win")) {
            fileName = "chromedriver.exe";
        } else if (!platform.contains("mac") && !platform.contains("linux")) {
            logger.fatal(String.format("Unsupported platform: %1$s for chrome browser %n", new Object[]{platform}));
        } else {
            fileName = "chromedriver";
        }
        File caps = new File(ProjectFilesProvider.getFileAbsolutePath("/src/test/resources/" + fileName));
        System.setProperty("webdriver.chrome.driver", caps.getAbsolutePath());
        DesiredCapabilities caps1 = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable("browser", Level.ALL);
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        caps1.setCapability("loggingPrefs", logPrefs);
        caps1.setCapability("chrome.switches", Arrays.asList("--disable-popup-blocking"));

        return new ChromeDriver(options);
    }

    private static RemoteWebDriver getIEDriver(Proxy proxy) {
        File myFile = null;
        // now remote connection will be refused, so use selenium server instead
        return new InternetExplorerDriver();
    }

    private static RemoteWebDriver getSafariDriver(DesiredCapabilities capabilities) {
        if (capabilities != null) {
            return new SafariDriver(capabilities);
        }
        return new SafariDriver();
    }
}
