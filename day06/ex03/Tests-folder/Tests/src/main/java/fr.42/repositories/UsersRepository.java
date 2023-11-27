package fr._42.numbers.repositories;

import fr._42.numbers.exceptions.AlreadyAuthenticatedException;
import fr._42.numbers.models.User;
import javax.persistence.EntityNotFoundException;

public interface UsersRepository {
    User findByLogin(String login) throws EntityNotFoundException;

    void update(User user) throws EntityNotFoundException;
}
