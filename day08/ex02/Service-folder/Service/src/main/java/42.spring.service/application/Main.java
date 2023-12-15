package fr.fortytwo.spring.service.application;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import fr.fortytwo.spring.service.models.User;
import fr.fortytwo.spring.service.repositories.UsersRepository;
import fr.fortytwo.spring.service.repositories.UsersRepositoryJdbcImpl;
import fr.fortytwo.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import fr.fortytwo.spring.service.config.ApplicationConfig;
import fr.fortytwo.spring.service.services.UsersServiceImpl;
import fr.fortytwo.spring.service.services.UsersService;

public class Main {

    public static void main(String[] args) {
        // ApplicationContext context = new
        // AnnotationConfigApplicationContext(ApplicationConfig.class);
        // UsersService usersService = context.getBean( UsersService.class);
        // String password = usersService.signup("testing@gmail.com");
        // System.out.println("password: " + password);
    }
}