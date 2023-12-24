
package fr.fortytwo.sockets.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.server.services.MessageService;
import fr.fortytwo.sockets.server.services.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.HashMap;

@Component("server")
public class Server {

    // thread pool
    private final Integer THREAD_POOL_NUMBER = 10;
    private final UsersService usersService;
    private final ServerSocket serverSocket;
    private final MessageService messageService;
    // user name - connection thread
    private final Map<String, UserConnection> onlineClients;
    // thread pool
    private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_NUMBER);

    @Autowired
    public Server(@Qualifier("serverSocket") ServerSocket serverSocket,
            @Qualifier("usersServiceImpl") UsersService usersService,
            @Qualifier("executorService") ExecutorService executorService,
            @Qualifier("messageServiceImpl") MessageService messageService) {
        this.serverSocket = serverSocket;
        this.usersService = usersService;
        this.onlineClients = new ConcurrentHashMap<>();
        this.executorService = executorService;
        this.messageService = messageService;
    }

    public void initServer() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client.getInetAddress().getHostAddress());
                UserConnection userConnection = new UserConnection(client, this);
                executorService.submit(userConnection); // Use executor to manage thread
            } catch (IOException e) {
                System.err.println("Err: " + e.getMessage());
            }
        }
    }

    // adding users to the online clients map
    public void userSignedIn(User user, UserConnection connection) {
        onlineClients.put(user.getName(), connection);
        System.out.println("User " + user.getName() + " signed in.");
    }

    // removing users from the online clients map
    public void userSignedOut(User user) {
        if (onlineClients.remove(user.getName()) != null) {
            System.out.println("User " + user.getName() + " signed out.");
        }
    }

    // broadcasting messages to online clients
    public void broadcastMessage(User sender, String message) {
        System.out.println("We're saving the message");
        String exactMessage = message.substring(message.indexOf("@") + 1);
        messageService.saveMessage(exactMessage, sender);
        for (UserConnection connection : onlineClients.values()) {
            // if not the sender, send the message
            if (!connection.getUser().getName().equals(sender.getName())) {
                connection.sendMessageToClient(sender.getName() + ": " + exactMessage);
            }
        }
    }

    // Get UserService instance
    public UsersService getUsersService() {
        return usersService;
    }

    // UserConnection thread
    public class UserConnection implements Runnable {
        private final Socket socket;
        private final Server server;
        private BufferedReader in;
        private PrintWriter out;
        private ObjectInputStream objectIn;
        private User currentUser;
        private boolean joinedRoom = false;

        public UserConnection(Socket socket, Server server) {
            this.socket = socket;
            this.server = server;
        }

        private void initializeConnection() throws IOException {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // objectIn = new ObjectInputStream(socket.getInputStream());
            System.out.println("We're initializing connection");
        }

        @Override
        public void run() {
            try {
                initializeConnection();
                // Send greeting to the client
                sendMessageToClient("Hello from Server!\n1. signIn\n2. signUp\n3. Exit");

                while (true) {
                    String action = in.readLine();
                    if (action == null)
                        break; // Client disconnected

                    switch (action) {
                        case "signIn":
                            signIn();
                            break;
                        case "signUp":
                            signUp();
                            break;

                        // case "listRooms":
                        // listRooms();
                        // break;
                        // case "logout":
                        // server.userSignedOut(currentUser);
                        // break;

                        case "chat":
                            chat();
                            break;
                        case "Exit":
                            server.userSignedOut(currentUser);
                            break;
                        default:
                            /// do nothing
                    }
                }
            } catch (IOException e) {
                System.err.println("Error in UserConnection: " + e.getMessage());
                // Handle exception, such as closing down the connection
            } finally {
                if (currentUser != null) {
                    server.userSignedOut(currentUser);
                }
                cleanUp();
            }
        }

        private boolean userIsOnline() throws IOException {
            return onlineClients.containsKey(currentUser.getName());
        }

        private List<Chatroom> listRooms() throws IOException {
            System.out.println("User wants to list rooms");
            if (!userIsOnline()) {
                sendMessageToClient("Authorization required.");
                return null;
            }
        }

        private void signIn() throws IOException {
            System.out.println("User wants to sign in");
            try {
                User user = (User) initObjectIn().readObject();
                Optional<User> validatedUser = server.getUsersService().signin(user.getName(), user.getPassword());
                // if user is online already send error message
                if (onlineClients.containsKey(user.getName())) {
                    sendMessageToClient("You're already have an active session");
                    return;
                }
                authenticatedUser(validatedUser);
            } catch (ClassNotFoundException e) {
                sendMessageToClient("Failed to deserialize user object.");
            } catch (Exception e) {
                sendMessageToClient("Sign-in failed: " + e.getMessage());
            }
        }

        private void signUp() throws IOException {
            System.out.println("User wants to sign up");
            try {
                User user = (User) initObjectIn().readObject();
                System.out.println("Object read from socket: " + user);
                Optional<User> createdUser = server.getUsersService().signup(user.getName(), user.getPassword());
                authenticatedUser(createdUser);
            } catch (ClassNotFoundException e) {
                sendMessageToClient("Failed to deserialize user object.");
            } catch (Exception e) {
                sendMessageToClient("Sign-up failed: " + e.getMessage());
            }
        }

        private void authenticatedUser(Optional<User> user) {
            System.out.println("trying to authenticate user");
            if (user.isPresent()) {
                this.currentUser = user.get();
                server.userSignedIn(currentUser, this);
                sendMessageToClient("Authentication successful.");
                // sendMessageToClient("Start messaging");
            } else {
                sendMessageToClient("Authentication failed.");
            }
        }

        private void chat() throws IOException {
            String message = in.readLine();
            server.broadcastMessage(currentUser, message);
        }

        private void sendMessageToClient(String message) {
            out.println(message);
        }

        private void cleanUp() {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (objectIn != null) {
                    objectIn.close();
                }
            } catch (IOException e) {
                System.err.println("Error cleaning up UserConnection: " + e.getMessage());
            }
        }

        private ObjectInputStream initObjectIn() throws IOException {
            if (objectIn == null) {
                objectIn = new ObjectInputStream(socket.getInputStream());
            }
            return objectIn;
        }

        public User getUser() {
            return currentUser;
        }

    }

}
