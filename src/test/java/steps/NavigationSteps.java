package steps;

import com.google.inject.Inject;
import cucumber.api.java.en.When;
import framework.Application;
import vk.Navigation;

public class NavigationSteps {
    private Application application;

    @Inject
    public NavigationSteps(Application app) {
        application = app;
    }

    @When("^I navigate to Login page$")
    public void navigateToLoginPage() {
        Navigation.openLoginPage();
    }
}
