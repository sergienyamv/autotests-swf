package steps;

import com.adobe.genie.executor.Genie;
import com.adobe.genie.executor.GenieLocatorInfo;
import com.adobe.genie.executor.LogConfig;
import com.adobe.genie.executor.components.GenieComponent;
import com.adobe.genie.genieCom.SWFApp;
import com.google.inject.Inject;
import cucumber.api.java.en.When;
import framework.Application;
import framework.Logger;
import framework.genie.GenieServer;
import vk.Navigation;

public class NavigationSteps {
    private Application application;

    @Inject
    public NavigationSteps(Application app) {
        application = app;
    }

    @When("^I navigate to Login page$")
    public void navigateToLoginPage() throws Exception {
        Navigation.openLoginPage();
        GenieServer.startServer(null);
        Genie g = Genie.init(new LogConfig());
        Thread.sleep(5000);

        for (SWFApp swfApp : g.getSWFList()) {
            Logger.getInstance().info(swfApp.name + " " + swfApp.preloadName + " " + swfApp.realName);
            swfApp.saveAppXml(swfApp.name.substring(1, 10) + ".xml");
            Logger.getInstance().info(swfApp.connect());
        }

        SWFApp app = g.connectToApp("DCLoader5");
        GenieComponent[] children = new GenieComponent("", app).getChildren(new GenieLocatorInfo(), true);

        for (GenieComponent component : children) {
            Logger.getInstance().info(component.getGenieID());
        }

        Thread.sleep(5000);
        GenieServer.stopServer();
    }
}
