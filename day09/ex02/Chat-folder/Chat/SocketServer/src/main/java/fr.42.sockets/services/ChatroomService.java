package fr.fortytwo.sockets.server.services;

import java.util.Optional;
import java.util.List;
import fr.fortytwo.sockets.models.Chatroom;
import fr.fortytwo.sockets.models.Message;

public interface ChatroomService {

    public void saveChatroom(String name);

    public List<Chatroom> getChatrooms();

    public void deleteChatroom(Long id);

    public void updateChatroom(Long id, String name);

    public List<Message> getRecentMessages(Long id);

    public void joinUserToRoom(Long userId, String chatroomName);

}
