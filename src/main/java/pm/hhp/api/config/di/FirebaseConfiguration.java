package pm.hhp.api.config.di;

import com.google.gson.JsonParser;
import io.jsonwebtoken.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import pm.hhp.api.config.authentication.AuthenticationFilter;
import pm.hhp.api.config.authentication.firebase.FirebaseAuthenticationFilter;
import pm.hhp.api.config.authentication.firebase.FirebaseAuthenticationUtils;
import pm.hhp.api.config.authentication.firebase.FirebasePublicKeys;
import pm.hhp.api.security.UnauthorizedAuthenticationEntryPoint;

import java.security.cert.CertificateFactory;

@Configuration
@Profile("!test")
public class FirebaseConfiguration {
  @Autowired
  private FirebasePublicKeys firebasePublicKeys;

  @Bean
  public FirebaseAuthenticationUtils firebaseAuthenticationUtils(
          JwtParser jwtParser,
          CertificateFactory certificateFactory
  ) {
    return new FirebaseAuthenticationUtils(
            firebasePublicKeys,
            jwtParser,
            new JsonParser(),
            certificateFactory
    );
  }

  @Bean
  public AuthenticationFilter firebaseAuthenticationFilter(
          FirebaseAuthenticationUtils firebaseAuthenticationUtils,
          UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint
  ) throws Exception {
    return new FirebaseAuthenticationFilter(
            firebaseAuthenticationUtils,
            new BearerTokenExtractor(),
            unauthorizedAuthenticationEntryPoint
    );
  }
}
