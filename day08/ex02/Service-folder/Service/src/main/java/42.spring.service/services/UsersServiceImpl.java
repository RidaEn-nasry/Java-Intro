
package fortytwo.spring.service.services;

import fortytwo.spring.service.services.UsersService;
import fortytwo.spring.service.repositories.UsersRepository;

public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public String signup(String email) {
        return new String("temprory password");
    }
}
