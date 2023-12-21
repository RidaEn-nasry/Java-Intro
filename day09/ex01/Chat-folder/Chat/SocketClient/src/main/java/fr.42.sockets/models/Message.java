
package fr.fortytwo.sockets.models;

import fr.fortytwo.sockets.models.User;

import java.io.Serializable;
import java.sql.Date;

public class Message implements Serializable {
    private Long id;
    private User sender;
    private String msg;
    private Date date;

    public Message() {
    }

    public Message(User sender, String msg, Date date) {
        this.sender = sender;
        this.msg = msg;
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public User getSender() {
        return this.sender;
    }

    public String getMsg() {
        return this.msg;
    }

    public Date getDate() {
        return this.date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message:{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", message='" + msg + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Message message = (Message) obj;
        return this.id == message.getId() && this.sender.equals(message.getSender())
                && this.msg.equals(message.getMsg()) && this.date.equals(message.getDate());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + this.sender.hashCode() + this.msg.hashCode() + this.date.hashCode();
    }

}