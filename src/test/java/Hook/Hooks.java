package Hook;

import io.cucumber.java.Before;
import stepDefinition.User_StepDef;

public class Hooks {

    @Before("@BeforeLogin")
    public void loginBeforeScenario() {
        User_StepDef userStepDef = new User_StepDef();
        userStepDef.loginUserUsing_username_And_password("jose2", "asdf");

    }
}
