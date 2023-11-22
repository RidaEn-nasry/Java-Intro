
package fr._42.chat.app;

import fr._42.chat.repositories.MessageRepository;
import fr._42.chat.repositories.MessageRepositoryJdbcImpl;
import fr._42.chat.models.Message;
import fr._42.chat.models.User;
import fr._42.chat.models.Chatroom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import com.zaxxer.hikari.HikariDataSource;

public class Program {

    static private String dbUrl = "jdbc:postgresql://localhost:5432/testing_db";
    static private String username = "wa5ina";

    private static Optional<Message> findById(long messageId, HikariDataSource ds, MessageRepository messageRepository)
            throws SQLException {
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(username);
        Optional<Message> messOptional = messageRepository.findById(messageId);
        ds.close();
        return messOptional;
    }

    private static void printMessage(Message message) {
        LocalDateTime dateTime = LocalDateTime.of(message.getMessageDateTime().getYear(),
                message.getMessageDateTime().getMonth(), message.getMessageDateTime().getDayOfMonth(),
                message.getMessageDateTime().getHour(),
                message.getMessageDateTime().getMinute());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        System.out.println("Message : {" +
                "\nid=" + message.getMessageId() +
                "\nauthor={id=" + message.getMessageAuthor().getUserId() + ",login=\""
                + message.getMessageAuthor().getUserLogin() + "\",password=\""
                + message.getMessageAuthor().getPassword() + "\",createdRooms=null,rooms=null},"
                + "\nroom={id=" + message.getMessageRoom().getChatroomId() + ",name=\""
                + message.getMessageRoom().getChatroomName() + "\",creator=null,messages=null},"
                + "\ntext=\"" + message.getMessageText() + "\","
                + "\ndatetime=" + formattedDateTime + "\n}");
    }

    private static void getMessage(HikariDataSource ds, MessageRepository messageRepository) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter a message ID ");
            long messageId = Long.parseLong(scanner.nextLine());
            try {
                Optional<Message> messOptional = findById(messageId, ds, messageRepository);
                if (messOptional.isPresent()) {
                    Message message = messOptional.get();
                    printMessage(message);
                } else {
                    System.out.println("Message with id " + messageId + " not found");
                }
            } catch (Exception e) {
                System.err.println("An error occurred while accessing the database: " +
                        e.getMessage());
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid input, please enter a valid message id.");
        }
    }

    private static Message saveMessage(Message message, HikariDataSource ds, MessageRepository messageRepository)
            throws SQLException {
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(username);
        Message savedMessage = messageRepository.save(message);
        ds.close();
        return savedMessage;
    }

    public static void main(String args[]) {

        HikariDataSource ds = new HikariDataSource();
        MessageRepository messageRepository = new MessageRepositoryJdbcImpl(ds);
        // Getting a message
        // getMessage(ds, messageRepository);

        // Saving a message
        User author = new User(1L, "ahmed", "1234", new ArrayList<Chatroom>(), new ArrayList<Chatroom>());

        Chatroom chatroom = new Chatroom(1L, "General", author, new ArrayList<Message>());
        Message message = new Message(null, author, chatroom, "Hello World, it's siraj", LocalDateTime.now());

        try {
            Message savedMessage = saveMessage(message, ds, messageRepository);
            if (savedMessage == null) {
                System.out.println("Message not saved");
                return;
            }
            System.out.println(savedMessage.getMessageId());
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
