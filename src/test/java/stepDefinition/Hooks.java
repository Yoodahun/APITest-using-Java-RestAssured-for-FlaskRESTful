package stepDefinition;

import factory.UserFactory;
import io.cucumber.java.Before;
import stepDefinition.User_StepDef;

public class Hooks {

    @Before(value = "@BeforeLogin")
    public void loginBeforeScenario() {
        User_StepDef userStepDef = new User_StepDef();

        if (UserFactory.getUser() != null) {
            UserFactory.clearUser();
        }
        userStepDef.user_api("Login");
        userStepDef.loginUserUsing_username_And_password("jose2", "asdf");
        userStepDef.saveAccess_tokenAndRefresh_tokenInResponseObject();

    }
}
