
package fr.fortytwo.sockets.server.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;
import java.sql.Date;
import fr.fortytwo.sockets.models.Message;
import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.server.repositories.MessageRepository;

@Service("messageServiceImpl")
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    @Qualifier("messageRepositoryImpl")
    MessageRepository messageRepository;


    @Override
    public void saveMessage(String msg) {
        Message newMesssage = new Message(msg, new Date(System.currentTimeMillis()));
        System.out.println("Message to be saved: " + newMesssage);
        messageRepository.save(newMesssage);
    }

    @Override
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @Override 
    public void deleteMessage(Long id) {
        messageRepository.delete(id);
    }



    @Override
    public void updateMessage(Long id, String msg) {
        Message newMessage = messageRepository.findById(id);
        newMessage.setMsg(msg);
        messageRepository.update(newMessage);
    }



}
