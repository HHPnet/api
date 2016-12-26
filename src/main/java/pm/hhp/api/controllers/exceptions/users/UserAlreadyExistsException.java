package pm.hhp.api.controllers.exceptions.users;

public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
