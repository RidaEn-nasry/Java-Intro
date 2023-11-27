
package fr._42.numbers.models;

import fr._42.numbers.models.Product;

public class User {
    private long id;
    private String login;
    private String password;
    private boolean authenticated;

    public User() {

    }

    public User(long id, String login, String password, boolean authenticated) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.authenticated = authenticated;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.login;
    }

    public void setName(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPrice(String password) {
        this.password = password;
    }

    public boolean getAuthenticated() {
        return this.authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }

        User u = (User) o;
        return (this.id == u.id) && (this.login.equals(u.login)) && (this.password.equals(u.password))
                && (this.authenticated == u.authenticated);
    }

    @Override
    public int hashCode() {
        return (int) (this.id + this.login.hashCode() + this.password.hashCode() + (this.authenticated ? 1 : 0));
    }

    @Override
    public String toString() {
        return "User[" + this.id + ", " + this.login + ", " + this.password + ", " + this.authenticated + "]";
    }

}