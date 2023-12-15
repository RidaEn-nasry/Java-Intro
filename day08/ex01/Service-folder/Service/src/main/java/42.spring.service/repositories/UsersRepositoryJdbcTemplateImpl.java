
package fr.fortytwo.spring.service.repositories;

import fr.fortytwo.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import fr.fortytwo.spring.service.models.User;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private DataSource ds;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private void removeAllUser() {
        String sql = "TRUNCATE TABLE users RESTART IDENTITY CASCADE";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public UsersRepositoryJdbcTemplateImpl(DataSource ds) {
        this.ds = ds;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        // to reset the table
        this.removeAllUser();
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        User user = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, (rs, rowNum) -> {
            User tmpUser = new User();
            tmpUser.setId(rs.getLong("id"));
            tmpUser.setEmail(rs.getString("email"));
            return tmpUser;
        });
        return user;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> {
            User tmpUser = new User();
            tmpUser.setId(rs.getLong("id"));
            tmpUser.setEmail(rs.getString("email"));
            return tmpUser;
        });
        return users;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email) VALUES (:email)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("email", user.getEmail());
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email = :email WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("email", user.getEmail()).addValue("id",
                user.getId());
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        SqlParameterSource namedParameters = new MapSqlParameterSource("email", email);
        User user = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, (rs, rowNum) -> {
            User tmpUser = new User();
            tmpUser.setId(rs.getLong("id"));
            tmpUser.setEmail(rs.getString("email"));
            return tmpUser;
        });

        return Optional.ofNullable(user);
    }
}