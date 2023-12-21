
package fr.fortytwo.sockets.server.services;

import fr.fortytwo.sockets.models.User;

public interface UsersService {

    public User signup(String name, String password);

    public User signin(String name, String password);

}