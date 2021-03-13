import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features = "src/test/java/features/Scenario_Z01_HappyScenario.feature",
        glue = "stepDefinition" )
public class TestRunner extends AbstractTestNGCucumberTests {
}
