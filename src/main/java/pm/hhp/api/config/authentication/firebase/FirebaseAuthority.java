package pm.hhp.api.config.authentication.firebase;

import org.springframework.security.core.GrantedAuthority;

public class FirebaseAuthority implements GrantedAuthority {
  @Override
  public String getAuthority() {
    return "Firebase";
  }
}
