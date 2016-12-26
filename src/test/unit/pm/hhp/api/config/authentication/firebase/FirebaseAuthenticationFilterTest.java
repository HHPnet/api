package pm.hhp.api.config.authentication.firebase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import pm.hhp.api.security.UnauthorizedAuthenticationEntryPoint;
import pm.hhp.core.services.users.UserRequest;
import pm.hhp.core.services.users.UserResponse;
import pm.hhp.core.services.users.getprofile.GetUserProfileByEmailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FirebaseAuthenticationFilterTest {
  private static final String AUTHENTICATION_PRINCIPAL = "principal";

  private static final String AUTHENTICATION_CREDENTIALS = "credentials";

  private FirebaseAuthenticationFilter firebaseAuthenticationFilter;

  @Mock
  private FirebaseAuthenticationUtils firebaseAuthenticationUtils;

  @Mock
  private BearerTokenExtractor tokenExtractor;

  @Mock
  private UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint;

  @Mock
  private Authentication authentication;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain chain;

  @Mock
  private GrantedAuthority grantedAuthority;

  @Mock
  private GetUserProfileByEmailService getUserProfileByEmailService;

  @Mock
  private UserResponse userResponse;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    firebaseAuthenticationFilter = new FirebaseAuthenticationFilter(
            firebaseAuthenticationUtils,
            tokenExtractor,
            unauthorizedAuthenticationEntryPoint,
            getUserProfileByEmailService
    );

    SecurityContextHolder.clearContext();
  }

  @After
  public void tearDown() throws Exception {
    firebaseAuthenticationFilter = null;
    firebaseAuthenticationUtils = null;
    tokenExtractor = null;
    unauthorizedAuthenticationEntryPoint = null;
    authentication = null;
    request = null;
    chain = null;
    grantedAuthority = null;
    getUserProfileByEmailService = null;
    userResponse = null;
  }

  @Test
  public void itAuthenticatesAValidUserGivenAnAccessToken() throws ServletException, IOException {
    doReturn(authentication).when(tokenExtractor).extract(request);
    doReturn(authentication).when(firebaseAuthenticationUtils).authenticate(authentication);
    doReturn(AUTHENTICATION_PRINCIPAL).when(authentication).getPrincipal();
    doReturn(AUTHENTICATION_CREDENTIALS).when(authentication).getCredentials();
    doReturn(Collections.singletonList(grantedAuthority)).when(authentication).getAuthorities();
    doReturn(userResponse).when(getUserProfileByEmailService).execute(any(UserRequest.class));

    firebaseAuthenticationFilter.doFilter(request, response, chain);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isInstanceOf(UsernamePasswordAuthenticationToken.class);
    verify(chain, times(1)).doFilter(request, response);
  }

  @Test
  public void itRedirectsToUnauthorizedEntryPointWhenInvalidTokenProvided() throws ServletException, IOException {
    doReturn(authentication).when(tokenExtractor).extract(request);
    doThrow(new UnauthorizedUserException("Unauthorized")).when(firebaseAuthenticationUtils).authenticate(authentication);

    firebaseAuthenticationFilter.doFilter(request, response, chain);
    verify(unauthorizedAuthenticationEntryPoint, times(1)).commence(eq(request), eq(response), any(AuthenticationException.class));
  }

  @Test
  public void itDoesNothingWhenNotUserCanBeAuthenticatedFromToken() throws ServletException, IOException {
    doReturn(authentication).when(tokenExtractor).extract(request);
    doReturn(null).when(firebaseAuthenticationUtils).authenticate(authentication);

    firebaseAuthenticationFilter.doFilter(request, response, chain);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(chain, times(1)).doFilter(request, response);
  }
}