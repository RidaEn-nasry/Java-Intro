package fr.fortytwo.sockets.client.models;

public class User implements Serializable {
    private String name;
    private String password;
    private Long id;

    public User() {
        this.name = "";
        this.password = "";
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
    }

    public User(String name, String password, Long id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("Please provide a non-empty name");
        }
        this.name = name;
    }

    public void setPassword(String password) {
        if (password.length() == 0) {
            throw new IllegalArgumentException("Please provide a non-empty password");
        }
        this.password = password;
    }

    public void setId(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Please provide a non-negative id");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", password=" + password + ", id=" + id + "]";
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
