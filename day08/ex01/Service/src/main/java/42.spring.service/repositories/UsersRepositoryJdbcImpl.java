
package fortytwo.spring.service.repositories;

import javax.sql.DataSource;
import fortytwo.spring.service.repositories.UsersRepository;
import java.util.List;
import fortytwo.spring.service.models.User;
import java.util.Optional;
import java.util.ArrayList;

public class UsersRepositoryJdbcImpl implements UsersRepository {

    private DataSource ds = null;

    public UsersRepositoryJdbcImpl(DataSource ds) {
        this.ds = ds;
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    @Override
    public User findById(Long id) {
        return new User();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<User>(1);
        return users;
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