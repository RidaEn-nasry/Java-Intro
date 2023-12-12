package fortytwo.spring.service.repositories;

import java.util.Optional;
import fortytwo.spring.service.repositories.CrudRepository;
import fortytwo.spring.service.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);
}
