package pm.hhp.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pm.hhp.core.services.UserSession;
import pm.hhp.core.services.users.UserResponse;

import java.util.Objects;

public class UserSessionImpl implements UserSession {
  @Override
  public boolean isLoggedInUserId(String userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return !Objects.isNull(authentication)
            && !Objects.isNull(authentication.getPrincipal())
            && userId.equals(((UserResponse) authentication.getPrincipal()).getUserId());
  }

  @Override
  public boolean isLoggedInUserEmail(String email) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return !Objects.isNull(authentication)
            && (
                    (
                            !Objects.isNull(authentication.getPrincipal())
                            && email.equals(((UserResponse) authentication.getPrincipal()).getEmail())
                    )
            || (
                            (
                                    !Objects.isNull(authentication.getCredentials())
                                    && email.equals(authentication.getCredentials())
                            )
                    )
    );
  }
}