package pm.hhp.api.config.authentication.firebase;

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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter implements AuthenticationFilter {
  private final FirebaseAuthenticationUtils firebaseAuthenticationUtils;

  private final BearerTokenExtractor tokenExtractor;

  private final UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint;

  public FirebaseAuthenticationFilter(
          FirebaseAuthenticationUtils firebaseAuthenticationUtils,
          BearerTokenExtractor tokenExtractor,
          UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint
  ) {
    this.firebaseAuthenticationUtils = firebaseAuthenticationUtils;
    this.tokenExtractor = tokenExtractor;
    this.unauthorizedAuthenticationEntryPoint = unauthorizedAuthenticationEntryPoint;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    try {
      authenticate(request);
      filterChain.doFilter(request, response);
    } catch (AuthenticationException | UnauthorizedUserException ex) {
      unauthorizedAuthenticationEntryPoint.commence(request, response, new AuthenticationException(ex.getMessage()) {});
    }
  }

  @Override
  public void authenticate(HttpServletRequest request) {
    Authentication authenticatedUser = firebaseAuthenticationUtils.authenticate(tokenExtractor.extract(request));
    if (!Objects.isNull(authenticatedUser) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              authenticatedUser.getPrincipal(),
              authenticatedUser.getCredentials(),
              authenticatedUser.getAuthorities()
      );

      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }
}
