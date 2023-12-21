package fr.fortytwo.sockets.client.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import fr.fortytwo.sockets.client.models.User;

public class AuthenticationClient {

    private User user;
    private Socket socket;

    public AuthenticationClient() {

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

    private void sendUser(User user, Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(user);
    }

    public AuthenticationClient(Socket socket) {
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
        System.out.println("Enter username: ");
        System.out.print("> ");
        String name = scanner.nextLine();
        System.out.println("Enter password: ");
        System.out.print("> ");
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
                    sendUser(user, socket);
                    // System.out.println("Hey");
                    break;
                default:
                    System.out.println("Please provide a valid command");
                    start();
                    // throw new IllegalArgumentException("Please provide a valid command");

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
