
package fr._42.chat.app;

import fr._42.chat.repositories.MessageRepository;
import fr._42.chat.repositories.MessageRepositoryJdbcImpl;
import fr._42.chat.models.Message;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;
import com.zaxxer.hikari.HikariDataSource;

public class Program {

    static private String dbUrl = "jdbc:postgresql://localhost:5432/testing_db";
    static private String username = "wa5ina";

    public static void main(String args[]) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(username);
        try (Scanner scanner = new Scanner(System.in)) {
            MessageRepository messageRepository = new MessageRepositoryJdbcImpl(ds);
            System.out.println("Enter a message ID ");
            long messageId = Long.parseLong(scanner.nextLine());
            try {
                Optional<Message> messOptional = messageRepository.findById(messageId);
                if (messOptional.isPresent()) {
                    Message message = messOptional.get();
                    LocalDateTime dateTime = LocalDateTime.of(message.getMessageDateTime().getYear(),
                            message.getMessageDateTime().getMonth(), message.getMessageDateTime().getDayOfMonth(),
                            message.getMessageDateTime().getHour(),
                            message.getMessageDateTime().getMinute());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
                    String formattedDateTime = dateTime.format(formatter);
                    System.out.println("Message : {" +
                            "\nid=" + messageId +
                            "\nauthor={id=" + message.getMessageAuthor().getUserId() + ",login=\""
                            + message.getMessageAuthor().getUserLogin() + "\",password=\""
                            + message.getMessageAuthor().getPassword() + "\",createdRooms=null,rooms=null},"
                            + "\nroom={id=" + message.getMessageRoom().getChatroomId() + ",name=\""
                            + message.getMessageRoom().getChatroomName() + "\",creator=null,messages=null},"
                            + "\ntext=\"" + message.getMessageText() + "\","
                            + "\ndatetime=" + formattedDateTime + "\n}");
                } else {
                    System.out.println("Message with id " + messageId + " not found");
                }
            } catch (

            Exception e) {
                System.err.println("An error occurred while accessing the database: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (

        NumberFormatException e) {
            System.err.println("Invalid input, please enter a valid message id.");
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
}
