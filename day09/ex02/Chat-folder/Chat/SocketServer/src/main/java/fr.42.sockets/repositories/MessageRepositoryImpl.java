package fr.fortytwo.sockets.server.repositories;

import fr.fortytwo.sockets.server.repositories.MessageRepository;

import java.sql.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.fortytwo.sockets.models.Message;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.server.repositories.UsersRepository;

@Repository("messageRepositoryImpl")
public class MessageRepositoryImpl implements MessageRepository {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;
    private UsersRepository usersRepository;

    @Autowired
    public MessageRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplateObject) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = jdbcTemplateObject;
        // to reset the table
        // this.resetDatabase();
    }

    private void resetDatabase() {
        // removing and creating the tables again
        this.jdbcTemplateObject.execute("DROP TABLE IF EXISTS messages");
        // long id, String msg, Date date, and User sender
        this.jdbcTemplateObject
                .execute("CREATE TABLE messages (id SERIAL PRIMARY KEY, msg VARCHAR(200), date DATE, sender INTEGER)");

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
    public Message findById(Long id) {
        String SQL = "select * from messages where id = ?";
        Message message = (Message) jdbcTemplateObject.queryForObject(SQL, new MessageMapper(), id);
        return message;
    }

    @Override
    public List<Message> findAll() {
        String SQL = "select * from messages";
        List<Message> messages = jdbcTemplateObject.query(SQL, new MessageMapper());
        return messages;
    }

    @Override
    public Message save(Message message) {
        String SQL = "insert into messages (msg, date, sender) values (?, ?, ?) ";
        int rowsAffected = jdbcTemplateObject.update(SQL, message.getMsg(), message.getDate(),
                message.getSender().getId());
        return message;
    }

    @Override
    public void update(Message message) {
        String SQL = "update messages set msg = ?, date = ?, sender = ? where id = ?";
        jdbcTemplateObject.update(SQL, message.getMsg(), message.getDate(), message.getSender().getId(),
                message.getId());
    }

    @Override
    public void delete(Long id) {
        String SQL = "delete from messages where id = ?";
        jdbcTemplateObject.update(SQL, id);
    }

}