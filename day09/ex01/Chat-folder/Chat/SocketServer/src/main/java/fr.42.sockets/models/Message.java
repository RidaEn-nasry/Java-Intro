
package fr.fortytwo.sockets.models;

import fr.fortytwo.sockets.models.User;

import java.io.Serializable;
import java.sql.Date;

public class Message implements Serializable {
    private Long id;
    private String msg;
    private Date date;

    public Message() {
    }

    public Message(Long id, String msg, Date date) {
        this.id = id;
        this.msg = msg;
        this.date = date;
    }

    public Message( String msg, Date date) {
        this.msg = msg;
        this.date = date;
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

    @Override
    public String toString() {
        return "Message:{" +
                "id=" + id +
                ", message='" + msg + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Message message = (Message) obj;
        return this.id == message.getId() && 
             this.msg.equals(message.getMsg()) && this.date.equals(message.getDate());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + this.msg.hashCode() + this.date.hashCode();
    }

}