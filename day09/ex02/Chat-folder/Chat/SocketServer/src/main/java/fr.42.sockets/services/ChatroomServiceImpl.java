
package fr.fortytwo.sockets.server.services;

import fr.fortytwo.sockets.server.repositories.ChatroomRepository;
import fr.fortytwo.sockets.server.repositories.MessageRepository;
import fr.fortytwo.sockets.server.services.ChatroomService;
import fr.fortytwo.sockets.models.Chatroom;
import fr.fortytwo.sockets.models.Message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatroomServiceImpl")
public class ChatroomServiceImpl implements ChatroomService {

    private static final int RECENT_MESSAGES_COUNT = 50;
    @Autowired
    ChatroomRepository chatroomRepository;

    @Override
    public void saveChatroom(String name) {
        Chatroom newChatroom = new Chatroom(name);
        chatroomRepository.save(newChatroom);

    }

    @Override
    public List<Chatroom> getChatrooms() {
        return chatroomRepository.findAll();
    }

    @Override
    public void deleteChatroom(Long id) {
        chatroomRepository.delete(id);
    }

    @Override
    public void updateChatroom(Long id, String name) {
        Chatroom newChatroom = chatroomRepository.findById(id);
        newChatroom.setName(name);
        chatroomRepository.update(newChatroom);
    }

    @Override
    public List<Message> getRecentMessages(Long id) {
        return chatroomRepository.getRecentMessages(id, RECENT_MESSAGES_COUNT);
    }

    @Override
    public void joinUserToRoom(Long userId, String chatroomName) { 
        chatroomRepository.joinUserToRoom(userId, chatroomName);
    }

}
