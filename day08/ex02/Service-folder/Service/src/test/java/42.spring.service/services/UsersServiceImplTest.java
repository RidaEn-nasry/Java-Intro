
package fr.fortytwo.spring.service.services;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.fortytwo.spring.service.repositories.UsersRepository;
import fr.fortytwo.spring.service.services.UsersService;
import fr.fortytwo.spring.service.config.TestApplicationConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertNotNull;
import java.util.Optional;

public class UsersServiceImplTest {

    private UsersRepository usersRepository;

    private UsersService usersService;

    @Autowired
    @Qualifier("embeddedDatabaseDataSource")
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestApplicationConfig.class);
        this.usersRepository = context.getBean("usersRepositoryJdbcTemplateImpl", UsersRepository.class);
        this.usersService = context.getBean(UsersService.class);
    }

    @Test
    public void testSignup() throws Exception {
        String password = this.usersService.signup("hey@gmail.com");
        assertNotNull(password);
    }

}