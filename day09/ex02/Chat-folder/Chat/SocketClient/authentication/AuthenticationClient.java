package fr.fortytwo.sockets.client.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import fr.fortytwo.sockets.models.User;

public class AuthenticationClient {
    private User user;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream objectOutputStream;

    private void printGreetingCommands() throws IOException {
        for (int i = 0; i <= 3; i++) {
            System.out.println(this.in.readLine());
        }
    }

    public AuthenticationClient(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        // server's greeting
        printGreetingCommands();

    }

    private void sendUserObject(User user) throws IOException {
        if (this.objectOutputStream == null) {
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        }
        this.objectOutputStream.writeObject(user);
    }

    private User promptUserDetails(Scanner scanner) {
        System.out.println("Enter username: ");
        System.out.print("> ");
        String name = scanner.nextLine().trim();
        System.out.println("Enter password: ");
        System.out.print("> ");
        String password = scanner.nextLine().trim();
        return new User(name, password);
    }

    private sendLogoutSignal() {
        this.out.println("logout");
        this.objectOutputStream.writeObject(this.user)  ;
        

        // this.user = null;
        // System.out.println("You've logged out.");
    }

    private List<Chatroom> getChatrooms() {
        this.out.println("getChatrooms");
        List<Chatroom> chatrooms = (List<Chatroom>) this.objectInputStream.readObject();
        return chatrooms;
    }

    private void handleJoinRoom(Scanner scanner) throws IOException {
        // first get the list of chatrooms
        // then prompt the user to choose a chatroom
        // then send the chatroom object to the server
        // then enter the chatroom

        // action to get rooms is
        List<Chatroom> chatrooms = getChatrooms();

    }

    // room actions
    private void handleRoomsActions(Scanner scanner) throws IOException {
        // 1. Create room
        // 2. Choose room
        // 3. Exit

        String action = scanner.nextLine().trim();
        switch (action) {
            case "1":
                break;
            case "2":
                handleJoinRoom(scanner);
            case "3":
                // this.out.println("logout");
                // this.objectOutputStream.writeObject(this.user);
                // this.user = null;
                // System.out.println("You've logged out.");
                break;
            default:
                break;
        }

    }

    private void authorizeUser(String action, Scanner scanner) throws IOException, ClassNotFoundException {
        User authUser = promptUserDetails(scanner);
        // send user's action to the server
        this.out.println(action);
        // a bit of artificial delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
        sendUserObject(authUser);
        String response = this.in.readLine();
        if ("Authentication successful.".equals(response)) {
            this.user = authUser;

            // show rooms actions

            enterChat(scanner);
        } else {
            System.out.println(response);
        }
    }

    private void enterChat(Scanner scanner) throws IOException {

        System.out.println("Start messaging");

        Thread incomingMessages = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (socket.getInputStream().available() > 0) {
                            String inComingMsg = in.readLine();
                            System.out.println();
                            System.out.println(inComingMsg);
                            System.out.print("> ");
                        }
                    } catch (IOException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            }
        });
        incomingMessages.start();
        while (true) {
            System.out.print("> ");
            // No need to check socket's InputStream availability; BufferedReader#readLine
            // is blocking
            String input = scanner.nextLine().trim();
            if ("Exit".equals(input)) {
                this.out.println("Exit");
                System.out.println("You have left the chat.");
                break;
            }
            // send action
            this.out.println("chat");
            // sends name@message to the server
            this.out.println(user.getName() + "@" + input);
        }
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String action = "";
                synchronized (scanner) {
                    action = scanner.nextLine().trim();
                }
                if ("1".equals(action) || "2".equals(action)) {
                    authorizeUser(action.equals("1") ? "signIn" : "signUp", scanner);
                } else if ("3".equals(action)) {
                    // clean up and exit
                    this.cleanUp();
                    System.out.println("Bye!");
                    break;
                } else {
                    System.out.println("Invalid action, valid actions are 1, 2, 3");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            cleanUp();
        }
    }

    private void cleanUp() {
        try {
            if (socket != null)
                socket.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (IOException e) {
            System.err.println("Error cleaning up: " + e.getMessage());
        }
    }

}
