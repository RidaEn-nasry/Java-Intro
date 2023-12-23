package fr.fortytwo.sockets.server.services;

import java.util.Optional;

import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.server.repositories.UsersRepository;
import java.util.List;
import fr.fortytwo.sockets.models.Message;

public interface MessageService {

    public void saveMessage(String message);

    public List<Message> getMessages();


    public void deleteMessage(Long id);

    public void updateMessage(Long id, String message);


}
