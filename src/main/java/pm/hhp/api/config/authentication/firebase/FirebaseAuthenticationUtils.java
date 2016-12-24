package pm.hhp.api.config.authentication.firebase;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class FirebaseAuthenticationUtils {
  private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationUtils.class);

  private final FirebasePublicKeys firebasePublicKeys;

  private final JwtParser jwtParser;

  private final JsonParser jsonParser;

  private final CertificateFactory certificateFactory;

  public FirebaseAuthenticationUtils(
          FirebasePublicKeys firebasePublicKeys,
          JwtParser jwtParser,
          JsonParser jsonParser,
          CertificateFactory certificateFactory
  ) {
    this.firebasePublicKeys = firebasePublicKeys;
    this.jwtParser = jwtParser;
    this.jsonParser = jsonParser;
    this.certificateFactory = certificateFactory;
  }

  Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (Objects.nonNull(authentication)) {
      for (Map.Entry<String, JsonElement> entry: getPublicKeysJson().entrySet()) {
        try {
          Claims claims = jwtParser.setSigningKey(getPublicKey(entry)).parseClaimsJws(authentication.getPrincipal().toString()).getBody();

          return new PreAuthenticatedAuthenticationToken(
                  claims.get("name").toString(),
                  claims.get("email").toString(),
                  Collections.singletonList(new FirebaseAuthority())
          );
        } catch (UnsupportedEncodingException | CertificateException | ExpiredJwtException | UnsupportedJwtException
                | MalformedJwtException | SignatureException | IllegalArgumentException e) {
          logger.warn(e.getMessage(), e);
        }
      }
    }

    throw new UnauthorizedUserException("Provided token not valid user");
  }

  private PublicKey getPublicKey(Map.Entry<String, JsonElement> entry) throws UnsupportedEncodingException, CertificateException {
    return (certificateFactory.generateCertificate(
                    new ByteArrayInputStream(entry.getValue().getAsString().getBytes("UTF-8"))
            )).getPublicKey();
  }

  private JsonObject getPublicKeysJson() {
    return jsonParser.parse(firebasePublicKeys.publicKeys()).getAsJsonObject();
  }
}
