package fr._42.chat.repositories;

import java.util.Optional;

import fr._42.chat.models.Message;

public interface MessageRepository {
    public Optional<Message> findById(long id);
}
