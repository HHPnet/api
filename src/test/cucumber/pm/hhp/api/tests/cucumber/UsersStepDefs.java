package pm.hhp.api.tests.cucumber;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.web.WebAppConfiguration;
import pm.hhp.core.model.users.UserFactory;
import pm.hhp.core.model.users.exceptions.UserNotAllowedToPerfomActionException;
import pm.hhp.core.services.users.UserRequest;
import pm.hhp.core.services.users.UserResponse;
import pm.hhp.core.services.users.create.CreateUserService;

import java.util.Collections;

@WebAppConfiguration
public class UsersStepDefs {
  @Autowired
  private CreateUserService createUserService;

  @Autowired
  private UserFactory userFactory;

  @Given("^users information:$")
  public void usersInformation(DataTable users) {
    users.asList(User.class).forEach(user -> {
      try {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new UserResponse(userFactory.getUserEntity(user.name, user.email)),
                        user.email,
                        Collections.emptyList()
                )
        );
        createUserService.execute(new UserRequest(user.name, user.email));
      } catch (UserNotAllowedToPerfomActionException ignored) {
        ignored.printStackTrace();
      }
    });
  }

  public class User {
    String name;

    String email;
  }
}
