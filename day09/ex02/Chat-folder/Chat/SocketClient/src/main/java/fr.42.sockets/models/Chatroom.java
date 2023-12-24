
package fr.fortytwo.sockets.models;

import java.io.Serializable;
import java.util.List;

import fr.fortytwo.sockets.models.Message;
import fr.fortytwo.sockets.models.User;

public class Chatroom implements Serializable {
    private String name;
    private Long id;
    private List<User> users;
    private List<Message> messages;

    public Chatroom() {
    }

    public Chatroom(String name, Long id, List<User> users, List<Message> messages) {
        this.name = name;
        this.id = id;
        this.users = users;
    }

    public Chatroom(String name, List<User> users, List<Message> messages) {
        this.name = name;
        this.users = users;
        this.messages = messages;
    }

    public Chatroom(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", users=" + users +
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Chatroom))
            return false;
        Chatroom other = (Chatroom) obj;
        return this.id == other.id && this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + this.name.hashCode();
    }

}
