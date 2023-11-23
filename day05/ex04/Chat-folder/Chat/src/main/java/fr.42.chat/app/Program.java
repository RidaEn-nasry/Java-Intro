
package fr._42.chat.app;

import fr._42.chat.repositories.UserRepository;
import fr._42.chat.repositories.UserRepositoryJdbcImpl;
import fr._42.chat.models.Message;
import fr._42.chat.models.User;
import fr._42.chat.models.Chatroom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import com.zaxxer.hikari.HikariDataSource;

public class Program {

    static private String dbUrl = "jdbc:postgresql://localhost:5432/testing_db";
    static private String username = "wa5ina";

    public static void main(String args[]) {

        HikariDataSource ds = new HikariDataSource();
        UserRepository userRepository = new UserRepositoryJdbcImpl(ds);
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(username);
        int[] sizes = { 1, 2, 3 };
        for (int size : sizes) {
            System.out.println("Testing with page size: " + size);
            for (int page = 0; page <= 5; page++) {
                System.out.println("Page: " + page);
                List<User> usersPage = userRepository.findAll(page, size);
                if (usersPage.isEmpty()) {
                    System.out.println("No more data is available on this page");
                    break;
                }
                for (User user : usersPage) {
                    // System.out.println("User ID: " + user.getUserId());

                    // System.out.println("Login: " + user.getUserLogin());
                    // System.out.println("Password: " + user.getPassword());
                    // System.out.println("Created Chatrooms IDs: " + user.getCreatedRooms());
                    // System.out.println("Joined Chatrooms IDs: " + user.getJoinedRooms());
                    System.out.println("User: " + user);
                    System.out.println();
                }
            }
            System.out.println();
        }
;
        ds.close();
    }

}
