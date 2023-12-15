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
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static void testUsersRepositoryJdbcTemplateImpl(
            UsersRepositoryJdbcTemplateImpl usersRepositoryJdbcTemplateImpl) {
        try {
            // testing save
            User newUser = new User();
            newUser.setEmail("testing@gmail.com");
            usersRepositoryJdbcTemplateImpl.save(newUser);
            // testing findAll
            System.out.println("*** Testing findAll ***");
            List<User> users = usersRepositoryJdbcTemplateImpl.findAll();
            for (User user : users) {
                System.out.println(user);
            }
            // testing update
            System.out.println("*** Testing update ***");
            User user = usersRepositoryJdbcTemplateImpl.findById(1L);
            user.setEmail("change@gmail.com");
            // user.setId(1L);
            usersRepositoryJdbcTemplateImpl.update(user);
            users = usersRepositoryJdbcTemplateImpl.findAll();
            for (User user1 : users) {
                System.out.println(user1);
            }
            // testing findByEmail
            System.out.println("*** Testing findByEmail ***");
            Optional<User> optionalUser = usersRepositoryJdbcTemplateImpl.findByEmail("change@gmail.com");
            System.out.println(optionalUser.get());
            // testing delete
            System.out.println("*** Testing delete ***");
            usersRepositoryJdbcTemplateImpl.delete(1L);
            users = usersRepositoryJdbcTemplateImpl.findAll();
            for (User user1 : users) {
                System.out.println(user1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testUsersRepositoryJdbcImpl(UsersRepositoryJdbcImpl usersRepositoryJdbcImpl) {
        // testing save
        User user = new User();
        user.setEmail("testing@gmail.com");
        usersRepositoryJdbcImpl.save(user);
        // testing findByID
        System.out.println("*** Testing findById ***");
        user = usersRepositoryJdbcImpl.findById(1L);
        System.out.println(user);
        // testing findAll
        System.out.println("*** Testing findAll ***");
        List<User> users = usersRepositoryJdbcImpl.findAll();
        for (User user1 : users) {
            System.out.println(user1);
        }
        // testing update
        System.out.println("*** Testing update ***");
        user.setEmail("changing@gmail.com");
        usersRepositoryJdbcImpl.update(user);
        user = usersRepositoryJdbcImpl.findById(1L);
        System.out.println(user);

        // testing findByEmail
        System.out.println("*** Testing findByEmail ***");
        Optional<User> optionalUser = usersRepositoryJdbcImpl.findByEmail("changing@gmail.com");
        System.out.println(optionalUser.get());

        // testing delete
        System.out.println("*** Testing delete ***");
        usersRepositoryJdbcImpl.delete(1L);
        users = usersRepositoryJdbcImpl.findAll();
    }

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        UsersRepository usersRepository = context.getBean("usersRepositoryJdbc",
                UsersRepository.class);

        User user = new User();
        user.setEmail("testing@gmail.com");
        usersRepository.save(user);

        // jdbc standard implementation with DriverManagerDataSource
        System.out.println(usersRepository.findAll());

        System.out.println("*****************************");
        // spring implementation with HickariCP
        usersRepository = context.getBean("usersRepositoryJdbcTemplate",
                UsersRepository.class);
        System.out.println(usersRepository.findAll());

        /******
         * jdbc standard implementation with DriverManagerDataSource without IOC
         ******/
        /*
         * DriverManagerDataSource dataSource = new DriverManagerDataSource();
         * dataSource.setDriverClassName("org.postgresql.Driver");
         * dataSource.setUrl("jdbc:postgresql://localhost:5432/database");
         * dataSource.setUsername("postgres");
         * dataSource.setPassword("qwerty007");
         **/

        /***
         * spring-jdbc implementation with DriverManagerDataSource without IOC
         **/
        /*
         * UsersRepositoryJdbcTemplateImpl usersRepositoryJdbcTemplateImpl = new
         * UsersRepositoryJdbcTemplateImpl(
         * dataSource);
         * testUsersRepositoryJdbcTemplateImpl(usersRepositoryJdbcTemplateImpl);
         * UsersRepositoryJdbcImpl usersRepositoryJdbcImpl = new
         * UsersRepositoryJdbcImpl(dataSource);
         * testUsersRepositoryJdbcImpl(usersRepositoryJdbcImpl);
         * usersRepositoryJdbcImpl.closeConnection();
         */
    }
}