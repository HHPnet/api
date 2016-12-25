package pm.hhp.api.tests.cucumber;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import pm.hhp.core.services.users.UserRequest;
import pm.hhp.core.services.users.create.CreateUserService;

@WebAppConfiguration
public class UsersStepDefs {
  @Autowired
  private CreateUserService createUserService;

  @Given("^users information:$")
  public void usersInformation(DataTable users) {
    users.asList(User.class).forEach(user -> createUserService.execute(new UserRequest(user.name, user.email)));
  }

  public class User {
    String name;

    String email;
  }
}
