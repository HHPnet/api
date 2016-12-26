package pm.hhp.api.dto.requests.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import pm.hhp.core.services.users.UserRequest;

public class CreateUserRequest extends UserRequest {

  public CreateUserRequest(
          @JsonProperty("name") String name,
          @JsonProperty("email") String email
  ) {
    super(name, email);
  }
}
