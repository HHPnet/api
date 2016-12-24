package pm.hhp.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pm.hhp.core.services.users.UserRequest;
import pm.hhp.core.services.users.UserResponse;
import pm.hhp.core.services.users.getprofile.GetUserProfileByEmailService;

import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping("/me")
public class MeController {
  @Autowired
  private GetUserProfileByEmailService getUserProfileByEmailService;

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<UserResponse> getUserProfile() {
    UserResponse userResponse = getUserProfileByEmailService.execute(new UserRequest(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),
            SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
    ));

    if (Objects.isNull(userResponse)) {
      throw new UsernameNotFoundException("User " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + " not found");
    }

    return new ResponseEntity<UserResponse>(userResponse, HttpStatus.ACCEPTED);
  }
}
