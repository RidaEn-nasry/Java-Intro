
package fr.fortytwo.sockets.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.fortytwo.sockets.models.User;
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
    public User signup(String name, String password) {
        if (name.length() == 0 || password.length() == 0) {
            throw new IllegalArgumentException("Please provide a non-empty name and password");
        }
        User user = new User(name, hashPassword(password));
        return usersRepository.save(user);
    }

    @Override
    public User signin(String name, String password) {
        if (name.length() == 0 || password.length() == 0) {
            throw new IllegalArgumentException("Please provide a non-empty name and password");
        }
        User user = usersRepository.findByName(name);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!passwordEncoder.matches(hashPassword(password), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }

}
