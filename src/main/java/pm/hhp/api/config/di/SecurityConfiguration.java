package pm.hhp.api.config.di;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import pm.hhp.api.config.authentication.AuthenticationFilter;
import pm.hhp.api.security.UnauthorizedAuthenticationEntryPoint;
import pm.hhp.api.security.UserSessionImpl;
import pm.hhp.core.services.UserSession;

import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Autowired
  private AuthenticationFilter authenticationFilter;

  @Bean
  public UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint() {
    return new UnauthorizedAuthenticationEntryPoint();
  }

  @Bean
  public CertificateFactory certificateFactory() throws CertificateException {
    return CertificateFactory.getInstance("X.509");
  }

  @Bean
  public JwtParser jwtParser() {
    return Jwts.parser();
  }

  @Bean
  public UserSession userSession() {
    return new UserSessionImpl();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
            .exceptionHandling().authenticationEntryPoint(unauthorizedAuthenticationEntryPoint())
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests().anyRequest().authenticated()
            .and().csrf().disable()
            .addFilterBefore(authenticationFilter, AnonymousAuthenticationFilter.class);
  }
}
