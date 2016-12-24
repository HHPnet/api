package pm.hhp.api.config.authentication;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

public interface AuthenticationFilter extends Filter {
  public void authenticate(HttpServletRequest request);
}
