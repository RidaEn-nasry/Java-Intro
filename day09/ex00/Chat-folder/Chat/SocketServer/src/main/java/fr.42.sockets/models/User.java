
package fr.fortytwo.sockets.server.models;

import java.io.Serializable;

public class User implements Serializable {
    Long id;
    String name;
    String password;

    public User() {

    }

    public User(Long id, String name, String password) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return this.id == user.getId() && this.name.equals(user.getName()) && this.password.equals(user.getPassword());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + this.name.hashCode() + this.password.hashCode();
    }

}