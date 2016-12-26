package pm.hhp.api.tests.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, glue = {"pm.hhp.api.tests.cucumber"})
@ActiveProfiles("test")
@IfProfileValue(name = "ENV", value = "test")
public class RunCukesTest  {
}
