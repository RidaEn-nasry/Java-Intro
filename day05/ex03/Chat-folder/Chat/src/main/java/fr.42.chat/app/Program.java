
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
        Optional<Message> messOptional = messageRepository.findById(messageId);
        ds.close();
        return messOptional;
    }

    private static void printMessage(Message message) {
        String formattedDateTime = null;
        if (message.getMessageDateTime() != null) {
            LocalDateTime dateTime = LocalDateTime.of(message.getMessageDateTime().getYear(),
                    message.getMessageDateTime().getMonth(), message.getMessageDateTime().getDayOfMonth(),
                    message.getMessageDateTime().getHour(),
                    message.getMessageDateTime().getMinute());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
            formattedDateTime = dateTime.format(formatter);
        }
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
        Message savedMessage = messageRepository.save(message);
        ds.close();
        return savedMessage;
    }

    public static boolean update(Long messageId, HikariDataSource ds, MessageRepository messageRepository)
            throws SQLException {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setMessageText("Bey Bye");
            message.setMessageDateTime(null);
            messageRepository.update(message);
            return true;
        }
        ;
        return false;
    }

    public static void main(String args[]) {

        HikariDataSource ds = new HikariDataSource();
        MessageRepository messageRepository = new MessageRepositoryJdbcImpl(ds);
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(username);
        // Getting a message

        /*
         * getMessage(ds, messageRepository);
         */

        // Saving a message

        /*
         * 
         * User author = new User(1L, "ahmed", "1234", new ArrayList<Chatroom>(), new
         * ArrayList<Chatroom>());
         * 
         * Chatroom chatroom = new Chatroom(1L, "General", author, new
         * ArrayList<Message>());
         * Message message = new Message(null, author, chatroom, "Hello World, it's
         * siraj", LocalDateTime.now());
         * 
         * try {
         * Message savedMessage = saveMessage(message, ds, messageRepository);
         * if (savedMessage == null) {
         * System.out.println("Message not saved");
         * return;
         * }
         * System.out.println(savedMessage.getMessageId());
         * } catch (Exception e) {
         * System.err.println("Err: " + e.getMessage());
         * e.printStackTrace();
         * }
         * 
         */

        // printing the message before update
        Optional<Message> messageOptional = messageRepository.findById(11L);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            printMessage(message);
        } else {
            System.out.println("Message with id " + 11L + " not found");
        }

        // update message
        try {
            boolean updated = update(11L, ds, messageRepository);
            if (updated) {
                System.out.println("Message updated");
            } else {
                System.out.println("Message not updated");
            }
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
        }

        // printing the message after update
        messageOptional = messageRepository.findById(11L);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            printMessage(message);
        } else {
            System.out.println("Message with id " + 11L + " not found");
        }
        ds.close();
    }
}
