package pm.hhp.api.config.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pm.hhp.api.infrastructure.repositories.jdbc.UserRepositoryJdbcImpl;
import pm.hhp.api.infrastructure.repositories.jdbc.mappers.UserRowMapper;
import pm.hhp.core.model.users.UserFactory;
import pm.hhp.core.model.users.UserRepository;
import pm.hhp.core.services.users.create.CreateUserService;
import pm.hhp.core.services.users.getprofile.GetUserProfileByEmailService;
import pm.hhp.core.services.users.getprofile.GetUserProfileService;

@Configuration
public class UsersConfiguration {
  @Bean
  public UserFactory userFactory() {
    return new UserFactory();
  }

  @Bean
  public UserRowMapper userRowMapper(UserFactory userFactory) {
    return new UserRowMapper(userFactory);
  }

  @Bean
  public UserRepository userRepository(
          NamedParameterJdbcTemplate jdbcTemplate,
          UserRowMapper userRowMapper
  ) {
    return new UserRepositoryJdbcImpl(jdbcTemplate, userRowMapper);
  }

  @Bean
  public CreateUserService createUserService(
          UserRepository repository,
          UserFactory factory
  ) {
    return new CreateUserService(repository, factory);
  }

  @Bean
  public GetUserProfileService getUserProfileService(
          UserRepository repository,
          UserFactory factory
  ) {
    return new GetUserProfileService(repository, factory);
  }

  @Bean
  public GetUserProfileByEmailService getUserProfileByEmailService(
          UserRepository repository,
          UserFactory factory
  ) {
    return new GetUserProfileByEmailService(repository, factory);
  }
}
