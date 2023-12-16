
package fr.fortytwo.sockets.server.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import fr.fortytwo.sockets.server.repositories.UsersRepository;
import fr.fortytwo.sockets.server.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Component("usersRepositoryImpl")
public class UsersRepositoryImpl implements UsersRepository {

    private DataSource ds;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(DataSource ds, JdbcTemplate jdbcTemplate) {
        this.ds = ds;
        this.jdbcTemplate = jdbcTemplate;
        // to reset the table
        this.resetDatabase();
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    private void resetDatabase() {
        // removing and creating the tables again
        this.jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        this.jdbcTemplate.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(50), password VARCHAR(50))");
    }

    private class UserRowMapper implements RowMapper {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = (User) this.jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        return user;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = this.jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
        return users;
    }

    @Override
    public void save(User user) {
        // it returns how many rows been affected
        // you might want to do something here
        int rowsAffected = this.jdbcTemplate.update("INSERT INTO users (name , password) VALUES (?, ?)", user.getName(),
                user.getPassword());

    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ? , password = ? WHERE id = ?";
        int rowsAffected = this.jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = this.jdbcTemplate.update(sql, id);

    }

}