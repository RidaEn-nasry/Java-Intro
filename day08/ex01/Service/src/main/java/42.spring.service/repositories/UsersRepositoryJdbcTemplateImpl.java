
package fortytwo.spring.service.repositories;

import fortytwo.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import fortytwo.spring.service.models.User;
import java.util.Optional;
import java.util.ArrayList;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private DataSource ds;
    private Connection connection;

    public UsersRepositoryJdbcTemplateImpl(DataSource ds) {
        this.ds = ds;
        try {
            this.connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    @Override
    public User findById(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            ps = this.connection.prepareStatement(sql);
            ps = this.connection.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(rs.getLong("id"), rs.getString("email"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        // String sql = "SELECT * from users;"
        // return ArrayList<User>(1);
        return new ArrayList<User>(1);
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

}