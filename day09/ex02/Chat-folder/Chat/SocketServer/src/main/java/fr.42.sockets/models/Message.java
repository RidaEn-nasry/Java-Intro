
package fr.fortytwo.sockets.models;

import fr.fortytwo.sockets.models.User;

import java.io.Serializable;
import java.sql.Date;

public class Message implements Serializable {
    private Long id;
    private String msg;
    private Date date;
    private User sender;

    public Message() {
    }

    public Message(Long id, String msg, Date date, User sender) {
        this.id = id;
        this.msg = msg;
        this.date = date;
        this.sender = sender;
    }

    public Message(String msg, Date date, User sender) {
        this.msg = msg;
        this.date = date;
        this.sender = sender;
    }

    public Long getId() {
        return this.id;
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

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Message:{" +
                "id=" + id +
                ", message='" + msg + '\'' +
                ", date='" + date + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Message message = (Message) obj;
        return this.id == message.getId() &&
                this.msg.equals(message.getMsg()) && this.date.equals(message.getDate()) &&
                this.sender.equals(message.getSender());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + this.msg.hashCode() + this.date.hashCode() + this.sender.hashCode();
    }

}