package pm.hhp.api.config.authentication.firebase;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "firebasePublicKeys", url = "${google.publicKeys}")
public interface FirebasePublicKeys {
  @RequestMapping(method = RequestMethod.GET)
  String publicKeys();
}
