
package fr._42.numbers.services;

import fr._42.numbers.exceptions.AlreadyAuthenticatedException;
import fr._42.numbers.models.User;
import fr._42.numbers.repositories.UsersRepository;

public class UsersServiceImpl {

    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean authenticate(String login, String password) {
        User user = this.usersRepository.findByLogin(login);
        if (user == null) {
            return false;
        }
        if (user.getAuthenticated()) {
            throw new AlreadyAuthenticatedException("User already authenticated");
        }
        if (user.getPassword().equals(password)) {
            user.setAuthenticated(true);
            this.usersRepository.update(user);
            return true;
        }
        return false;
    }

}
