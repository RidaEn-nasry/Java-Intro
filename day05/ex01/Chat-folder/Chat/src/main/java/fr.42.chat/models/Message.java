
package fr._42.chat.models;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

import fr._42.chat.models.Chatroom;
import fr._42.chat.models.User;

public class Message {
    private long messageId;
    private User messageAuthor;
    private Chatroom messageRoom;
    private String messageText;
    private LocalDateTime messageDateTime;


    public Message() {

    }

    public Message(long messageId, User messageAuthor, Chatroom messageRoom, String messageText,
                   LocalDateTime messageDateTim) {
        this.messageId = messageId;
        this.messageAuthor = messageAuthor;
        this.messageRoom = messageRoom;
        this.messageText = messageText;
        this.messageDateTime = messageDateTim;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public User getMessageAuthor() {
        return this.messageAuthor;
    }


    public void setMessageAuthor(User messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    public Chatroom getMessageRoom() {
        return this.messageRoom;
    }


    public void setMessageRoom(Chatroom messageRoom) {
        this.messageRoom = messageRoom;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getMessageDateTime() {
        return this.messageDateTime;
    }

    public void setMessageDateTime(LocalDateTime messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass())
            return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId) && Objects.equals(messageAuthor, ((Message) o).messageAuthor) &&
                Objects.equals(messageRoom, ((Message) o).messageRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, messageAuthor, messageRoom);
    }


    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + +this.messageId +
                ", messageAuthor='" + this.messageAuthor + '\'' +
                ", messageRoom='" + this.messageRoom +
                ", messageText=" + messageText + '}';
    }

}
