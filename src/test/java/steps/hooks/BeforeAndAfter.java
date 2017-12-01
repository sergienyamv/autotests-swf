package steps.hooks;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import framework.Logger;
import framework.common.functions.DateFunctions;
import framework.selenium.Browser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BeforeAndAfter {

    @Before(order = 1)
    public void beforeScenario(Scenario info) {
        Logger.getInstance().logTestName(info.getName());
        Browser.getInstance();
        Browser.getInstance().windowMaximise();
    }

    @After(order = 1)
    public void afterScenario(Scenario result) throws IOException {
        String pathToFile = Browser.getInstance().makeScreen("scenario_" + DateFunctions.getCurrentDate("ddMMyyyyHHmmss"));
        Path path = Paths.get(pathToFile);
        byte[] data = Files.readAllBytes(path);
        result.embed(data, "image/png");
        Browser.getInstance().exit();
        Logger.getInstance().logTestEnd(result.getName(), result.getStatus());
    }
}
