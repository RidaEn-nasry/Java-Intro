package fr._42.numbers.services;

import fr._42.numbers.exceptions.AlreadyAuthenticatedException;
import fr._42.numbers.models.User;
import fr._42.numbers.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.Mockito.*;
import javax.persistence.EntityNotFoundException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UsersServiceImplTest {
    private UsersRepository usersRepository;
    private UsersServiceImpl usersServiceImpl;

    @BeforeEach
    public void setUp() {
        this.usersRepository = mock(UsersRepository.class);
        this.usersServiceImpl = new UsersServiceImpl(this.usersRepository);
    }

    @Test
    public void testAuthenticateWithCorrectCredentials() {
        User user = new User(1, "login", "password", false);
        Mockito.when(this.usersRepository.findByLogin("login")).thenReturn(user);
        boolean result = this.usersServiceImpl.authenticate("login", "password");
        assertTrue(result);
        Mockito.verify(usersRepository, Mockito.times(1)).update(user);
    }

    @Test
    public void testAuthenticateWithIncorrectLogin() {
        Mockito.when(this.usersRepository.findByLogin("something")).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class,
                () -> {
                    this.usersServiceImpl.authenticate("something", "password");
                });
    }

    @Test
    public void testAuthenticateWithIncorrectPassword() {
        User user = new User(1, "login", "password", false);
        Mockito.when(this.usersRepository.findByLogin("login")).thenReturn(user);
        boolean result = this.usersServiceImpl.authenticate("login", "something");
        assertFalse(result);
    }

    @Test
    public void testAuthenticateWithAlreadyAuthenticatedUser() {
        User user = new User(1, "login", "password", true);
        Mockito.when(this.usersRepository.findByLogin("login")).thenReturn(user);
        assertThrows(AlreadyAuthenticatedException.class,
                () -> {
                    this.usersServiceImpl.authenticate("login", "password");
                });
    }

}