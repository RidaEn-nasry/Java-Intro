
package fr.fortytwo.sockets.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.server.repositories.CrudRepository;
import fr.fortytwo.sockets.server.repositories.UsersRepository;
import fr.fortytwo.sockets.server.services.UsersService;

@Service("usersServiceImpl")
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String hashPassword(CharSequence password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Optional<User> signup(String name, String password) {
        if (name.length() == 0 || password.length() == 0) {
            throw new IllegalArgumentException("Please provide a non-empty name and password");
        }
        User user = null;
        try {
            user = usersRepository.findByName(name);
        } catch (Exception e) {
            // user's doesn't exist, it's ok
        }
        if (user != null) {
            System.out.println("User already exists: " + user);
            throw new IllegalArgumentException("User already exists");
        }
        user = new User(name, hashPassword(password));
        System.out.println("User to be created: " + user);
        User newUser = usersRepository.save(user);
        System.out.println("User created: " + newUser);
        return Optional.of(newUser);
    }

    @Override
    public Optional<User> signin(String name, String password) {
        if (name.length() == 0 || password.length() == 0) {
            throw new IllegalArgumentException("Please provide a non-empty name and password");
        }
        User user = usersRepository.findByName(name);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        return Optional.of(user);
    }
}
