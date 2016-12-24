package pm.hhp.api.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler({UsernameNotFoundException.class})
  public ResponseEntity<Object> handleUsernameNotFoundException(Exception ex, WebRequest request) {
    return new ResponseEntity<Object>("User not found", new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({UnauthorizedUserException.class})
  public ResponseEntity<Object> handleUnauthorizedUserException(Exception ex, WebRequest request) {
    return new ResponseEntity<Object>("Unauthorized access", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }
}
