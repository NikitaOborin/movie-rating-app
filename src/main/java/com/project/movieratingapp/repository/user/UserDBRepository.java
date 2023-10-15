package com.project.movieratingapp.repository.user;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class UserDBRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO users (name, login, email, birthday) " +
                          "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (user.getName() == null || user.getName().isEmpty() || user.getName().equals(" ")) {
            user.setName(user.getLogin());
            log.info("UserInMemoryRepository: addUser(): name=null, login assign for name");
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedStatement;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET name=?, login=?, email=?, birthday=? WHERE user_id=?";
        int countRow = jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(),
                                           user.getBirthday(), user.getId());

        if (countRow == 0) {
          throw new NotFoundException("user with id = " + user.getId() + " not found");
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        User user;
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE user_id=?", userRowMapper, id);

        if (!users.isEmpty()) {
            user = users.get(0);
        } else {
            throw new NotFoundException("user with id = " + id + " not found");
        }

        return user;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());

        return user;
    };
}
