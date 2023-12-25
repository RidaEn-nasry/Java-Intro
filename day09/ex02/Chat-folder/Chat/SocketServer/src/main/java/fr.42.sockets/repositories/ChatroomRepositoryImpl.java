
package fr.fortytwo.sockets.server.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import fr.fortytwo.sockets.models.Message;
import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.server.repositories.MessageRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.fortytwo.sockets.models.Chatroom;
import fr.fortytwo.sockets.server.repositories.ChatroomRepository;
import fr.fortytwo.sockets.server.repositories.UsersRepository;
import fr.fortytwo.sockets.server.repositories.MessageRepository;

@Repository("chatroomRepositoryImpl")
public class ChatroomRepositoryImpl implements ChatroomRepository {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private UsersRepository usersRepository;
    private MessageRepository messageRepository;

    @Autowired
    public ChatroomRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        // to reset the table
        // this.resetDatabase();
    }

    private void resetDatabase() {
        // removing and creating the tables again
        this.jdbcTemplate.execute("DROP TABLE IF EXISTS chatrooms");
        // long id, String name, List of ids of users , List of ids of messages
        this.jdbcTemplate.execute(
                "CREATE TABLE chatrooms (id SERIAL PRIMARY KEY, name VARCHAR(50), users INTEGER[], messages INTEGER[])");

    }

    private List<Long> convertSqlArrayToList(ResultSet rs, String columnName) throws SQLException {
        List<Long> ids = new ArrayList<Long>();
        Long[] idsArray = (Long[]) rs.getArray(columnName).getArray();
        for (Long id : idsArray) {
            ids.add(id);
        }
        return ids;
    }

    private List<User> getUsersFromIds(List<Long> ids) {
        List<User> users = new ArrayList<User>();
        for (Long id : ids) {
            users.add(usersRepository.findById(id));
        }
        return users;

    }

    private List<Message> getMessagesFromIds(List<Long> ids) {
        List<Message> messages = new ArrayList<Message>();
        for (Long id : ids) {
            messages.add(messageRepository.findById(id));
        }
        return messages;
    }

    private class ChatroomMapper implements RowMapper {
        public Chatroom mapRow(ResultSet rs, int rowNum) throws SQLException {
            Chatroom chatroom = new Chatroom();
            chatroom.setId(rs.getLong("id"));
            chatroom.setName(rs.getString("name"));
            return chatroom;
        }
    }

    // joing a user to room
    @Override
    public void joinUserToRoom(Long userId, String chatroomName) {
        String SQL = "update chatrooms set users = array_append(users, ?) where name = ?";
        jdbcTemplate.update(SQL, userId, chatroomName);
    }

    private class MessageMapper implements RowMapper {
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            User sender = usersRepository.findById(rs.getLong("sender"));
            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setMsg(rs.getString("msg"));
            message.setSender(sender);
            message.setDate(rs.getDate("date"));
            return message;
        }
    }

    @Override
    public List<Message> getRecentMessages(Long id, int count) {
        String SQL = "select * from messages where id = ? order by date desc limit ?";
        List<Message> messages = jdbcTemplate.query(SQL, new MessageMapper(), id, count);
        return messages;
    }

    @Override
    public Chatroom findById(Long id) {
        String SQL = "select * from chatrooms where id = ?";
        Chatroom chatroom = (Chatroom) jdbcTemplate.queryForObject(SQL, new ChatroomMapper(), id);
        return chatroom;

    }

    @Override
    public List<Chatroom> findAll() {
        String SQL = "select * from chatrooms";
        List<Chatroom> chatrooms = jdbcTemplate.query(SQL, new ChatroomMapper());
        System.out.println("Chatrooms: " + chatrooms);
        return chatrooms;
    }

    @Override
    public Chatroom save(Chatroom entity) {
        String SQL = "insert into chatrooms (name, users, messages) values (?, ?, ?) ";
        int rowsAffected = jdbcTemplate.update(SQL, entity.getName(), entity.getUsers(), entity.getMessages());
        return entity;
    }

    @Override
    public void delete(Long id) {
        String SQL = "delete from chatrooms where id = ?";
        jdbcTemplate.update(SQL, id);
    }

    @Override
    public void update(Chatroom entity) {
        String SQL = "update chatrooms set name = ?, users = ?, messages = ? where id = ?";
        jdbcTemplate.update(SQL, entity.getName(), entity.getUsers(), entity.getMessages(), entity.getId());

    }

    @Override
    public Optional<Chatroom> findByName(String name) {
        String SQL = "select * from chatrooms where name = ?";
        Chatroom chatroom = (Chatroom) jdbcTemplate.queryForObject(SQL, new ChatroomMapper(), name);
        return Optional.ofNullable(chatroom);
    }
}