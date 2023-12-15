
package fr.fortytwo.spring.service.services;

import fr.fortytwo.spring.service.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import fr.fortytwo.spring.service.models.User;
import fr.fortytwo.spring.service.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepositoryJdbcTemplateImpl") UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    private String generateTempPassword() {
        byte[] arr = new byte[7];
        for (int i = 0; i < 7; i++) {
            // in ASCII table: 97 - 122 , lower case letters
            arr[i] = (byte) (Math.random() * 26 + 97);
        }
        return new String(arr);
    }

    @Override
    public String signup(String email) {
        String password = this.generateTempPassword();
        User user = new User(email, password);
        this.usersRepository.save(user);
        return password;
    }
}
