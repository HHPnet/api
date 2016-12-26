package pm.hhp.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pm.hhp.api.controllers.exceptions.users.UserAlreadyExistsException;
import pm.hhp.api.dto.requests.users.CreateUserRequest;
import pm.hhp.core.model.users.exceptions.UserNotAllowedToPerfomActionException;
import pm.hhp.core.model.users.exceptions.UserNotFoundException;
import pm.hhp.core.services.users.UserRequest;
import pm.hhp.core.services.users.UserResponse;
import pm.hhp.core.services.users.create.CreateUserService;
import pm.hhp.core.services.users.getprofile.GetUserProfileByEmailService;
import pm.hhp.core.services.users.save.SaveUserService;

import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping("/me")
public class MeController {
  @Autowired
  private GetUserProfileByEmailService getUserProfileByEmailService;

  @Autowired
  private CreateUserService createUserService;

  @Autowired
  private SaveUserService saveUserService;

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<UserResponse> getUserProfile() {
    UserResponse userResponse = (UserResponse) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

    if (Objects.isNull(userResponse)) {
      throw new UsernameNotFoundException(
              "User " + SecurityContextHolder.getContext().getAuthentication().getCredentials()
              + " not found"
      );
    }

    return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<UserResponse> createUserProfile(@RequestBody CreateUserRequest userRequest)
          throws UserAlreadyExistsException, UserNotAllowedToPerfomActionException {
    UserResponse userResponse = createUserService.execute(userRequest);

    if (Objects.isNull(userResponse)) {
      throw new UserAlreadyExistsException(userRequest.getEmail());
    }

    return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.PUT)
  @ResponseBody
  public ResponseEntity<UserResponse> updateUserProfile(
          @RequestBody CreateUserRequest userRequest
  ) throws UserNotAllowedToPerfomActionException, UserNotFoundException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal())) {
      throw new UnauthorizedUserException("User not found");
    }

    UserResponse userResponse = saveUserService.execute(new UserRequest(
            ((UserResponse)authentication.getPrincipal()).getUserId(),
            userRequest.getName(),
            userRequest.getEmail()
    ));

    if (Objects.isNull(userResponse)) {
      throw new UserNotFoundException();
    }

    return new ResponseEntity<UserResponse>(userResponse, HttpStatus.ACCEPTED);
  }
}
