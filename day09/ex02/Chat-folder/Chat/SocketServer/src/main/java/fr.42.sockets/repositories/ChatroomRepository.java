package fr.fortytwo.sockets.server.repositories;

import fr.fortytwo.sockets.server.repositories.CrudRepository;
import fr.fortytwo.sockets.models.Chatroom;
import fr.fortytwo.sockets.models.Message;
import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends CrudRepository<Chatroom> {

    public Optional<Chatroom> findByName(String name);

    // get number of messages from a chatroom
    public List<Message> getRecentMessages(Long id, int number);

}
