
package fr.fortytwo.spring.service.repositories;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import fr.fortytwo.spring.service.repositories.UsersRepository;
import java.util.List;
import fr.fortytwo.spring.service.models.User;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("usersRepositoryJdbcImpl")
public class UsersRepositoryJdbcImpl implements UsersRepository {

    private DataSource ds;

    @Autowired
    public UsersRepositoryJdbcImpl(@Qualifier("driverManagerDataSource") DataSource ds) {
        // to reset the table
        this.ds = ds;
        this.resetDatabase();
    }

    private void resetDatabase() {
        Connection connection = null;
        // removing and creating the tables again
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection conn = this.ds.getConnection()) {
            // If autocommit is false, you need to commit the transaction manually.
            conn.setAutoCommit(false);

            // Drop the existing table if it exists
            try (PreparedStatement preparedStatement = conn.prepareStatement("DROP TABLE IF EXISTS users")) {
                preparedStatement.executeUpdate();
            }

            // Create the new table
            try (PreparedStatement preparedStatement = conn.prepareStatement(
                    "CREATE TABLE users (id SERIAL PRIMARY KEY, email VARCHAR(255), password VARCHAR(255));")) {
                preparedStatement.executeUpdate();
            }

            // Commit the changes to the database
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    @Override
    public User findById(Long id) {
        Connection connection = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return user;

    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        Connection connection = null;
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void save(User user) {
        Connection connection = null;
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email = ?, password = ? WHERE id = ?";
        Connection connection = null;
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        Connection connection = null;
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection connection = null;
        User user = null;
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

}