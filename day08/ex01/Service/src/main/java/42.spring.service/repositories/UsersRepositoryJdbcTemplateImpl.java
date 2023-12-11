
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
import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.jdbc.coNamedParameterJdbcTemplatere.namedparam.;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private DataSource ds;
    private Connection connection;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UsersRepositoryJdbcTemplateImpl(DataSource ds) {
        this.ds = ds;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    @Override
    public User findById(Long id) {
        
        User user = this.namedParameterJdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = :id",
            
            User.class
        );
        return user;
    }

    @Override
    public List<User> findAll() {

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