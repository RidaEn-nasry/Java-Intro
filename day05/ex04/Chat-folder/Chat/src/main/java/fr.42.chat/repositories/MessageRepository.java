package fr._42.chat.repositories;

import java.util.Optional;
import fr._42.chat.models.Message;
import fr._42.chat.exceptions.NotSavedSubEntityException;
import java.sql.SQLException;

public interface MessageRepository {
    public Optional<Message> findById(long id);

    public Message save(Message message) throws NotSavedSubEntityException, SQLException;

    public boolean update(Message message) throws SQLException;

}
