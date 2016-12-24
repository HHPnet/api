package pm.hhp.api.config.di.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import pm.hhp.api.config.authentication.AuthenticationFilter;
import pm.hhp.api.config.authentication.firebase.FakeFirebaseAuthenticationFilter;
import pm.hhp.api.security.UnauthorizedAuthenticationEntryPoint;

@Configuration
@Profile("test")
public class FakeFirebaseConfiguration {
  @Bean
  public AuthenticationFilter fakeFirebaseAuthenticationFilter(UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint) {
    return new FakeFirebaseAuthenticationFilter(
            new BearerTokenExtractor(),
            unauthorizedAuthenticationEntryPoint
    );
  }

}
