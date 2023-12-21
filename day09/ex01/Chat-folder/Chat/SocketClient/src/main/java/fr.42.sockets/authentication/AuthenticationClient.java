package fr.fortytwo.sockets.client.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import fr.fortytwo.sockets.models.User;

// import fr.fortytwo.sockets.server.models.User;

public class AuthenticationClient {

    private User user;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public AuthenticationClient() {

    }

    private void writeLineToSocket(String msg) {
        if (socket != null && socket.isConnected()) {
            out.println(msg);
        } else {
            System.err.println("Err: Socket hangup.");
            System.exit(1);
        }
    }

    private String readLineFromSocket() throws IOException {
        if (socket != null && socket.isConnected()) {
            try {

                String line = in.readLine();
                return line;
            } catch (IOException e) {
                System.err.println("Err " + e.getMessage());
            }
        } else {
            System.err.println("Err: Socket hangup.");
            System.exit(1);
        }
        return null;
    }

    private void sendUserAction(String action) {
        writeLineToSocket(action);
    }

    public AuthenticationClient(Socket socket) {
        this.socket = socket;
        String line = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(in.readLine());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void sendUser(User user) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(user);
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

    private void EnterChat() throws IOException, ClassNotFoundException {
        System.out.println("Start messaging");
        boolean inChat = true;
        while (inChat) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("Exit")) {
                sendUserAction(line);
                System.out.println("You have left the chat");
                System.exit(0);
            } else {
                sendUserAction(line);
                System.out.println(readLineFromSocket());
            }
        }
        // Scanner scanner = new Scanner(System.in);
        // User user = parseUserDetails(scanner);
        // sendUser(user, socket);
        // String response = getLineFromServer(socket);
        // System.out.println(response);
    }

    private void authorizeUser(String action, Scanner scanner) throws IOException, ClassNotFoundException {
        sendUserAction(action);
        sendUser(parseUserDetails(scanner));
        if (readLineFromSocket().equals("Start messaging")) {
            EnterChat();
        } else {
            System.out.println("Wrong username or password");
            start();
        }
    }

    public void start() {

        while (true) {
            if (socket == null) {
                System.out.println("Socket is null");
                System.exit(1);
            }
            try {
                System.out.print("> ");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                switch (line) {
                    case "signIn":
                        authorizeUser(line, scanner);
                        break;
                    case "signUp":
                        authorizeUser(line, scanner);
                        break;
                    case "Exit":
                        sendUserAction(line);
                        System.out.println("You have left the chat");
                        System.exit(0);

                    default:
                        System.out.println("Please provide a valid command");
                        start();
                }
            } catch (Exception e) {
                System.err.println("Err: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
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
