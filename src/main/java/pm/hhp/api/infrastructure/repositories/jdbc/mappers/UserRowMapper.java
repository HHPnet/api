package pm.hhp.api.infrastructure.repositories.jdbc.mappers;

import org.springframework.jdbc.core.RowMapper;
import pm.hhp.core.model.users.User;
import pm.hhp.core.model.users.UserFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
  private UserFactory userFactory;

  public UserRowMapper(UserFactory userFactory) {
    this.userFactory = userFactory;
  }

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    return userFactory.getUserEntity(
            rs.getString("user_id"),
            rs.getString("name"),
            rs.getString("email")
    );
  }
}
