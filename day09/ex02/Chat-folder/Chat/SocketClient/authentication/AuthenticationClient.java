package fr.fortytwo.sockets.client.authentication;

public interface AuthenticationClient {
    public User authenticate(String username, String password);

    public User register(String username, String password);

    public boolean logout(String username);

    public boolean isLoggedIn(String username);

    // a method to get user details, prompt user for details

    public User promptUserDetails();

}
