package framework.common.functions;

import framework.Configuration;
import framework.selenium.Browser;

public class NavigationFunctions {
    public static void navigateTo(String url) {
        Browser.getInstance().waitForPageToLoad();
        Browser.getInstance().navigate(url);
        if (!Browser.getInstance().getLocation().equals(url)) {
            Browser.getInstance().navigate(url);
        }
    }

    public static void navigateToStartPage() {
        navigateTo(Configuration.getCurrentEnvironment().getStartUrl());
    }
}