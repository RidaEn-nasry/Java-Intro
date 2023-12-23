
package fr.fortytwo.sockets.server.services;

import java.util.Optional;

import fr.fortytwo.sockets.models.User;

public interface UsersService {

    public Optional<User> signup(String name, String password);

    public Optional<User> signin(String name, String password);

}