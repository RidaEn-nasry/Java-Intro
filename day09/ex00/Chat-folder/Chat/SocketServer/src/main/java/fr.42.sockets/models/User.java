
package fr.fortytwo.sockets.server.models;

public class User {
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

}