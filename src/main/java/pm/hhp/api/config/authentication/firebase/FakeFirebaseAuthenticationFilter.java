package pm.hhp.api.config.authentication.firebase;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import pm.hhp.api.config.authentication.AuthenticationFilter;
import pm.hhp.api.security.UnauthorizedAuthenticationEntryPoint;
import pm.hhp.core.services.users.UserRequest;
import pm.hhp.core.services.users.getprofile.GetUserProfileByEmailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class FakeFirebaseAuthenticationFilter extends OncePerRequestFilter implements
        AuthenticationFilter {
  private static final String VALID_TOKEN = "USERTOKEN";

  private final BearerTokenExtractor tokenExtractor;

  private final UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint;

  private final GetUserProfileByEmailService getUserProfileByEmailService;

  public FakeFirebaseAuthenticationFilter(
          BearerTokenExtractor tokenExtractor,
          UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint,
          GetUserProfileByEmailService getUserProfileByEmailService
  ) {
    this.tokenExtractor = tokenExtractor;
    this.unauthorizedAuthenticationEntryPoint = unauthorizedAuthenticationEntryPoint;
    this.getUserProfileByEmailService = getUserProfileByEmailService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws
          ServletException, IOException {
    try {
      authenticate(request);
      filterChain.doFilter(request, response);
    } catch (AuthenticationException | UnauthorizedUserException ex) {
      unauthorizedAuthenticationEntryPoint.commence(
              request,
              response,
              new AuthenticationException(ex.getMessage()) {}
      );
    }
  }

  @Override
  public void authenticate(HttpServletRequest request) {
    Authentication authenticatedUser = tokenExtractor.extract(request);
    if (Objects.isNull(authenticatedUser) || !authenticatedUser.getPrincipal().toString()
            .startsWith(VALID_TOKEN)) {
      throw new BadCredentialsException("Not valid token provided");
    }

    String[] split = authenticatedUser.getPrincipal().toString().split("_");
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            getUserProfileByEmailService.execute(new UserRequest(
                    split[1].toLowerCase(),
                    split[2].toLowerCase()
            )),
            split[2].toLowerCase(),
            Collections.singletonList(new FirebaseAuthority())
    );

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
