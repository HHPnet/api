package pm.hhp.api.infrastructure.repositories.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pm.hhp.api.infrastructure.repositories.jdbc.mappers.UserRowMapper;
import pm.hhp.core.model.users.User;
import pm.hhp.core.model.users.exceptions.UserNotFoundException;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserRepositoryJdbcImplTest {
  private UserRepositoryJdbcImpl userRepositoryJdbc;

  @Mock
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Mock
  private UserRowMapper rowMapper;

  @Mock
  private User user;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    userRepositoryJdbc = new UserRepositoryJdbcImpl(jdbcTemplate, rowMapper);
  }

  @Test
  public void itIsPossibleToSaveAnUser() {
    doReturn(1).when(jdbcTemplate).update(anyString(), any(MapSqlParameterSource.class));
    doReturn(UUID.randomUUID().toString()).when(user).getUserId();

    assertThat(userRepositoryJdbc.save(user)).isEqualTo(user);
    verify(jdbcTemplate, times(1)).update(anyString(), any(MapSqlParameterSource.class));
  }

  @Test
  public void itIsPossibleToFindAnUserGivenItsId() throws UserNotFoundException {
    doReturn(Collections.singletonList(user))
            .when(jdbcTemplate)
            .query(anyString(), any(MapSqlParameterSource.class), any(UserRowMapper.class));


    assertThat(userRepositoryJdbc.findById(UUID.randomUUID().toString())).isEqualTo(user);
  }

  @Test(expected = UserNotFoundException.class)
  public void itFailsWhenGivenUserIdDoesNotExist() throws UserNotFoundException {
    doReturn(Collections.emptyList())
            .when(jdbcTemplate)
            .query(anyString(), any(MapSqlParameterSource.class), any(UserRowMapper.class));


    userRepositoryJdbc.findById(UUID.randomUUID().toString());
  }

  @Test
  public void itIsPossibleToFindAnUserGivenItsEmail() throws UserNotFoundException {
    doReturn(Collections.singletonList(user))
            .when(jdbcTemplate)
            .query(anyString(), any(MapSqlParameterSource.class), any(UserRowMapper.class));


    assertThat(userRepositoryJdbc.findByEmail("test")).isEqualTo(user);
  }

  @Test(expected = UserNotFoundException.class)
  public void itFailsWhenGivenEmailDoesNotExist() throws UserNotFoundException {
    doReturn(Collections.emptyList())
            .when(jdbcTemplate)
            .query(anyString(), any(MapSqlParameterSource.class), any(UserRowMapper.class));


    userRepositoryJdbc.findByEmail("test");
  }
}