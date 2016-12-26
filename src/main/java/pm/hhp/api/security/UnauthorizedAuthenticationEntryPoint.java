package pm.hhp.api.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class UnauthorizedAuthenticationEntryPoint implements
        AuthenticationEntryPoint, Serializable {
  private static final long serialVersionUID = -8970718410437077606L;

  @Override
  public void commence(
          HttpServletRequest request,
          HttpServletResponse response,
          AuthenticationException authException
  ) throws IOException, ServletException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}
