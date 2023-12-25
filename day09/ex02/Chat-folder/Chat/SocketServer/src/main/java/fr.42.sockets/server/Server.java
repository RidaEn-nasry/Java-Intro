
package fr.fortytwo.sockets.server.server;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import fr.fortytwo.sockets.models.Chatroom;
import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.server.services.MessageService;
import fr.fortytwo.sockets.server.services.UsersService;
import fr.fortytwo.sockets.models.Chatroom;
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

import fr.fortytwo.sockets.server.services.ChatroomService;

/*
 * standard response from server:
 * status: ok | notOk
 * message: string
 * data: object | null
 * 
 */
@Component("server")
public class Server {

    // thread pool
    private final Integer THREAD_POOL_NUMBER = 10;
    private final UsersService usersService;
    private final ServerSocket serverSocket;
    private final MessageService messageService;
    private final ChatroomService chatroomService;
    // user name - connection thread
    private final Map<String, UserConnection> onlineClients;
    // eahc suer and their joined room, name - room name
    private final Map<String, String> joinedRooms = new HashMap<>();
    // thread pool
    private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_NUMBER);

    @Autowired
    public Server(@Qualifier("serverSocket") ServerSocket serverSocket,
            @Qualifier("usersServiceImpl") UsersService usersService,
            @Qualifier("executorService") ExecutorService executorService,
            @Qualifier("messageServiceImpl") MessageService messageService,
            @Qualifier("chatroomServiceImpl") ChatroomService chatroomService) {
        this.serverSocket = serverSocket;
        this.usersService = usersService;
        this.onlineClients = new ConcurrentHashMap<>();
        this.executorService = executorService;
        this.messageService = messageService;
        this.chatroomService = chatroomService;
    }

    public void initServer() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = serverSocket.accept();
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
    }

    // removing users from the online clients map
    public void userSignedOut(User user) {
        onlineClients.remove(user.getName());
    }

    // broadcasting messages to online clients
    public void broadcastMessage(User sender, String message, String roomName) {

        messageService.saveMessage(message, sender);

        for (UserConnection connection : onlineClients.values()) {
            // if not the sender, send the message
            if (!connection.getUser().getName().equals(sender.getName())
                    && joinedRooms.get(sender.getName()).equals(roomName)) {
                JSONObject response = new JSONObject();
                response.put("status", "ok");
                response.put("message", message);
                response.put("sender", sender.getName());
                response.put("roomName", roomName);
                connection.sendMessageToClient(response.toString());
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
        // name of joined room
        private String joinedRoom;

        public UserConnection(Socket socket, Server server) {
            this.socket = socket;
            this.server = server;
        }

        private void initializeConnection() throws IOException {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // objectIn = new ObjectInputStream(socket.getInputStream());
        }

        // a method to assemble a response object
        private JSONObject assembleResponse(String status, String message, Object data) {
            JSONObject response = new JSONObject();
            response.put("status", status);
            response.put("message", message);
            response.put("data", data);
            return response;
        }

        private void enteredChatroom(JSONObject json) throws IOException {
            // enter chat room
            // add user to the joined room
            joinedRooms.put(currentUser.getName(), json.getString("roomName"));
            chatroomService.joinUserToRoom(currentUser.getId(), json.getString("roomName"));
            boolean joinedRoom = true;

            sendMessageToClient(assembleResponse("ok", "You've joined the room", null).toString());

            while (joinedRoom) {
                JSONObject data = new JSONObject(in.readLine());
                String action = data.getString("action");
                JSONObject dataObject = data.getJSONObject("data");
                switch (action) {
                    case "chat":
                        String message = dataObject.getString("message");
                        server.broadcastMessage(currentUser, message, dataObject.getString("roomName"));
                        break;
                    case "Exit":
                        // remove user from joined room
                        joinedRooms.remove(currentUser.getName());
                        joinedRoom = false;
                        break;
                    default:
                        break;
                }

            }
        }

        private void createRoom(JSONObject json) throws IOException {
            // if user is not authenticated
            if (!userIsOnline()) {
                sendMessageToClient(assembleResponse("notOk", "Authorization required.", null).toString());
                return;
            }

            try {

                Chatroom chatroom = new Chatroom(json.getString("roomName"));
                server.chatroomService.saveChatroom(chatroom.getName());
                sendMessageToClient(
                        assembleResponse("ok", "Room " + chatroom.getName() + " created successfully.", null)
                                .toString());
            } catch (Exception e) {
                sendMessageToClient(
                        assembleResponse("notOk", "Room creation failed: " + e.getMessage(), null).toString());
            }
        }

        private void listRooms() throws IOException {
            if (!userIsOnline()) {
                sendMessageToClient("Authorization required.");
                return;
            }

            try {

                List<Chatroom> chatrooms = server.chatroomService.getChatrooms();

                sendMessageToClient(assembleResponse("ok", "List of rooms", chatrooms).toString());
            } catch (Exception e) {
                sendMessageToClient(assembleResponse("notOk", "List of rooms failed: " + e.getMessage(), null)
                        .toString());
            }

            // sendMessageToClient(assembleResponse("ok", "List of rooms",
            // chatrooms).toString());
        }

        @Override
        public void run() {
            try {
                initializeConnection();
                // Send greeting to the client
                sendMessageToClient("Hello from Server!\n1. signIn\n2. signUp\n3. Exit");

                while (true) {
                    // String action = in.readLine();
                    JSONObject data = new JSONObject(in.readLine());
                    String action = data.getString("action");
                    if (action == null)
                        break; // Client disconnected

                    switch (action) {
                        case "signIn":
                            signIn(data.getJSONObject("data"));
                            break;
                        case "signUp":
                            signUp(data.getJSONObject("data"));
                            break;
                        case "createRoom":
                            createRoom(data.getJSONObject("data"));
                            break;
                        case "listRooms":
                            listRooms();
                            break;
                        case "joinRoom":
                            enteredChatroom(data.getJSONObject("data"));
                            break;

                        // case "chat":

                        // System.out.println("User wants to chat");
                        // String message = data.getString("message");
                        // server.broadcastMessage(currentUser, message, data.getString("roomName"));
                        // break;
                        case "Exit":
                            // remove user from online clients, and joined room
                            joinedRooms.remove(currentUser.getName());
                            userSignedOut(currentUser);

                            // server.userSignedOut(currentUser);
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

        private void signIn(JSONObject json) throws IOException {
            try {

                User user = new User(json.getString("name"), json.getString("password"));

                Optional<User> validatedUser = server.getUsersService().signin(user.getName(), user.getPassword());

                // if user is online already send error message
                if (onlineClients.containsKey(user.getName())) {
                    sendMessageToClient(
                            assembleResponse("notOk", "You're already have an active session", null).toString());
                    return;
                }
                authenticatedUser(validatedUser);

            } catch (

            Exception e) {
                sendMessageToClient(assembleResponse("notOk", "Sign-in failed: " + e.getMessage(), null).toString());
            }
        }

        private void signUp(JSONObject json) throws IOException {

            try {

                User user = new User(json.getString("name"), json.getString("password"));
                Optional<User> createdUser = server.getUsersService().signup(user.getName(), user.getPassword());
                authenticatedUser(createdUser);
            } catch (Exception e) {
                sendMessageToClient(assembleResponse("notOk", "Sign-up failed: " + e.getMessage(), null).toString());
            }
        }

        private void authenticatedUser(Optional<User> user) {

            if (user.isPresent()) {
                this.currentUser = user.get();
                server.userSignedIn(currentUser, this);
                // sendMessageToClient("Authentication successful.");
                sendMessageToClient(assembleResponse("ok", "Authentication successful.", null).toString());
                // sendMessageToClient("Start messaging");
            } else {
                sendMessageToClient(assembleResponse("notOk", "Authentication failed.", null).toString());
            }
        }

        // private void chat() throws IOException {
        // String message = in.readLine();
        // server.broadcastMessage(currentUser, message);
        // }

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
