package fr._42.chat.models;

import fr._42.chat.models.Message;
import fr._42.chat.models.User;

import java.util.List;
import java.util.Objects;

public class Chatroom {
    private long chatroomId;
    private String chatroomName;
    private User chatroomOwner;
    private List<Message> messages;

    public Chatroom() {

    }

    public Chatroom(long chatroomId, String chatroomName, User chatroomOwner, List<Message> messages) {
        this.chatroomId = chatroomId;
        this.chatroomName = chatroomName;
        this.chatroomOwner = chatroomOwner;
        this.messages = messages;
    }

    public long getChatroomId() {
        return this.chatroomId;
    }

    public void setChatroomId(long chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getChatroomName() {
        return this.chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public User getChatroomOwner() {
        return this.chatroomOwner;
    }

    public void setChatroomOwner(User chatroomOwner) {
        this.chatroomOwner = chatroomOwner;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /*
     * here we're using Objects.equals and not type.equals,
     * is because type might be null
     * in which case it will throw a NullPointerException
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Chatroom chatroom = (Chatroom) o;
        return Objects.equals(chatroomId, chatroom.chatroomId) && Objects.equals(chatroomName, chatroom.chatroomName) &&
                Objects.equals(chatroomOwner, chatroom.chatroomOwner);
    }

    ;

    @Override
    public int hashCode() {
        return Objects.hash(chatroomId, chatroomName, chatroomOwner);
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "chatroomId='" + chatroomId +
                ", chatroomName='" + chatroomName + '\'' +
                ", chatroomOwner='" + chatroomOwner +
                ", messages=" + messages + '}';
    }

}
