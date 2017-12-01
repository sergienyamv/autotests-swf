package vk;

import framework.Configuration;
import framework.common.functions.NavigationFunctions;

public class Navigation {
    private static String startPage = Configuration.getCurrentEnvironment().getStartUrl();
    private static String loginPage = String.format("%s", startPage);

    public static void openLoginPage() {
        NavigationFunctions.navigateTo(loginPage);
    }
}
