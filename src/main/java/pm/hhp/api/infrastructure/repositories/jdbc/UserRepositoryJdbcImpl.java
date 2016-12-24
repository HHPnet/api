package pm.hhp.api.infrastructure.repositories.jdbc;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pm.hhp.api.infrastructure.repositories.jdbc.mappers.UserRowMapper;
import pm.hhp.core.model.users.User;
import pm.hhp.core.model.users.UserRepository;
import pm.hhp.core.model.users.exceptions.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public class UserRepositoryJdbcImpl implements UserRepository {
  private NamedParameterJdbcTemplate jdbcTemplate;

  private UserRowMapper rowMapper;

  public UserRepositoryJdbcImpl(NamedParameterJdbcTemplate jdbcTemplate, UserRowMapper rowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.rowMapper = rowMapper;
  }

  @Override
  public User save(User user) {
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("user_id", UUID.fromString(user.getUserId()));
    parameters.addValue("name", user.getName());
    parameters.addValue("email", user.getEmail());

    jdbcTemplate.update("INSERT INTO users VALUES (:user_id, :name, :email)", parameters);

    return user;
  }

  @Override
  public User findById(String userId) throws UserNotFoundException {
    List<User> users = jdbcTemplate.query(
            "SELECT user_id, name, email FROM users WHERE user_id = :user_id",
            new MapSqlParameterSource("user_id", UUID.fromString(userId)),
            rowMapper
    );

    if (users.isEmpty()) {
      throw new UserNotFoundException();
    }

    return users.get(0);
  }

  @Override
  public User findByEmail(String email) throws UserNotFoundException {
    List<User> users = jdbcTemplate.query(
            "SELECT user_id, name, email FROM users WHERE email = :email",
            new MapSqlParameterSource("email", email),
            rowMapper
    );

    if (users.isEmpty()) {
      throw new UserNotFoundException();
    }

    return users.get(0);
  }
}
