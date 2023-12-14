package fortytwo.spring.service.application;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import fortytwo.spring.service.models.User;
import fortytwo.spring.service.repositories.UsersRepository;
import fortytwo.spring.service.repositories.UsersRepositoryJdbcImpl;
import fortytwo.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import fortytwo.spring.service.config.ApplicationConfig;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UsersRepository usersRepository = context.getBean(UsersRepositoryJdbcImpl.class);
        System.out.println("UsersRepository: " + usersRepository);

    }
}