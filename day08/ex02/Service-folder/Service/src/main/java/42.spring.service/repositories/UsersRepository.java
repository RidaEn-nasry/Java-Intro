package fr.fortytwo.spring.service.repositories;

import java.util.Optional;
import fr.fortytwo.spring.service.repositories.CrudRepository;
import fr.fortytwo.spring.service.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);
}
