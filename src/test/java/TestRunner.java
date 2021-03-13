import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/test/java/features",
        glue = "stepDefinition",
        tags = "@RegressionTest or @User or @login or @Store or @Item"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
