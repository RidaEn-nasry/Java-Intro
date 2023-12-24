
package fr.fortytwo.sockets.server.repositories;

import fr.fortytwo.sockets.server.repositories.CrudRepository;
import fr.fortytwo.sockets.models.User;

public interface UsersRepository extends CrudRepository<User> {

    public User findByName(String name);
}
