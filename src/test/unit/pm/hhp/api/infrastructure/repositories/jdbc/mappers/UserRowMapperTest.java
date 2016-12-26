package pm.hhp.api.infrastructure.repositories.jdbc.mappers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pm.hhp.core.model.users.User;
import pm.hhp.core.model.users.UserFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class UserRowMapperTest {
  private UserRowMapper userRowMapper;

  @Mock
  private UserFactory factory;

  @Mock
  private ResultSet resultSet;

  @Mock
  private User user;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    userRowMapper = new UserRowMapper(factory);
  }

  @Test
  public void itMapsForAGivenRowToAnUser() throws SQLException {
    doReturn("test").when(resultSet).getString(anyString());
    doReturn(user).when(factory).getUserEntity("test", "test", "test");

    assertThat(userRowMapper.mapRow(resultSet, 0)).isEqualTo(user);
  }
}