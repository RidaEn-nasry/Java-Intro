
package fr._42.chat.repositories;

import fr._42.chat.repositories.MessageRepository;

import fr._42.chat.models.User;
import fr._42.chat.models.Message;
import fr._42.chat.models.Chatroom;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import fr._42.chat.exceptions.NotSavedSubEntityException;

public class MessageRepositoryJdbcImpl implements MessageRepository {
    private DataSource dataSource;

    public MessageRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Message> findById(long id) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT m.message_id, m.message_text, m.message_datetime, u.user_id, u.login, u.password, c.chatroom_id, c.chatroom_name "
                    + "FROM messages m "
                    + "JOIN users u ON m.message_author = u.user_id "
                    + "JOIN chatrooms c ON m.message_room = c.chatroom_id "
                    + "WHERE m.message_id = ?";
            // create a pre-compiled statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User author = new User(resultSet.getLong("user_id"), resultSet.getString("login"),
                        resultSet.getString("password"), null, null);
                Chatroom chatroom = new Chatroom(resultSet.getLong("chatroom_id"), resultSet.getString("chatroom_name"),
                        null, null);
                Message message = new Message(resultSet.getLong("message_id"), author, chatroom,
                        resultSet.getString("message_text"),
                        resultSet.getTimestamp("message_datetime").toLocalDateTime());
                return Optional.of(message);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Message save(Message message) throws NotSavedSubEntityException, SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // if author or room is null we'll throw an execption
            if (message.getMessageAuthor() == null || message.getMessageRoom() == null) {
                throw new NotSavedSubEntityException("Message author and room must have an id");
            }
            // check room/author existence
            String sqlCheck = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?) AND EXISTS(SELECT 1 FROM chatrooms WHERE chatroom_id = ?)";
            PreparedStatement statementCheck = connection.prepareStatement(sqlCheck);
            statementCheck.setLong(1, message.getMessageAuthor().getUserId());
            statementCheck.setLong(2, message.getMessageRoom().getChatroomId());
            ResultSet resultSetCheck = statementCheck.executeQuery();
            if (resultSetCheck.next()) {
                if (!resultSetCheck.getBoolean(1)) {
                    throw new NotSavedSubEntityException("Message author and room must exist");
                }
            }

            String sql = "INSERT INTO messages (message_author, message_room,message_text, message_datetime) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setLong(1, message.getMessageAuthor().getUserId());
            statement.setLong(2, message.getMessageRoom().getChatroomId());
            statement.setString(3, message.getMessageText());
            statement.setTimestamp(4,
                    java.sql.Timestamp.valueOf(message.getMessageDateTime()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                message.setMessageId(resultSet.getLong(1));
                return message;
            }

        }

        return null;
    }
}
