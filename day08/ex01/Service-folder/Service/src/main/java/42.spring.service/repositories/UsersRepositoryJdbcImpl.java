
package fr.fortytwo.spring.service.repositories;

import javax.sql.DataSource;
import fr.fortytwo.spring.service.repositories.UsersRepository;
import java.util.List;
import fr.fortytwo.spring.service.models.User;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.PriorityQueue;
public class UsersRepositoryJdbcImpl implements UsersRepository {

    private DataSource ds = null;

    public UsersRepositoryJdbcImpl(DataSource ds) {
        this.ds = ds;
    }

    private void deleteAllUser() {
        Connection connection = null;
        String sql = "TRUNCATE TABLE users RESTART IDENTITY CASCADE";
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
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
        String sql = "INSERT INTO users (email) VALUES (?)";
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        Connection connection = null;
        try (Connection conn = this.ds.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setLong(2, user.getId());
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
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

}