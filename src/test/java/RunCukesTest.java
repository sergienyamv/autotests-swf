import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.CucumberOptions;

@CucumberOptions(
        format = {"pretty", "html:target/cucumber-html", "json:target/cucumber.json"},
        features = "src/test/java/tests/")
public class RunCukesTest extends AbstractTestNGCucumberTests {
}