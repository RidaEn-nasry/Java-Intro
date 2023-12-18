package fr.fortytwo.sockets.client.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.InputStreamReader;

import fr.fortytwo.sockets.client.models.User;

public class AuthenticationClient {

    private User user;
    private Socket socket;

    public Authentication() {

    }

    public String getLineFromServer(Socket socket) throws IOException {
        BufferedReader in = null;
        if (socket.isConnected()) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = in.readLine();
            return line;
        }
        return null;
    }

    public Authentication(Socket socket) {
        this.socket = socket;
        String line = null;
        try {
            line = getLineFromServer(socket);
            System.out.println(line);
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public User parseUserDetails(Scanner scanner) throws IOException {
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        User user = new User(name, password);
        return user;
    }

    public void start() {
        try {

            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            switch (line) {
                case "signUp":
                    User user = parseUserDetails(scanner);
                    System.out.println(user);
                    break;
                default:
                    throw new IllegalArgumentException("Please provide a valid command");

            }
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
