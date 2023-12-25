package fr.fortytwo.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.models.Chatroom;

public class Client {
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

    public Client(Socket socket) throws IOException {
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

    // to convert user object to json using JsonObject
    private User convertJsonToUser(JSONObject json, String action) {
        // get the data object
        JSONObject data = json.getJSONObject("data");
        String name = data.getString("name");
        String password = data.getString("password");
        return new User(name, password);
    }

    private JSONObject convertUserToJson(User user, String action) {
        JSONObject json = new JSONObject();
        json.put("action", action);
        JSONObject data = new JSONObject();
        data.put("name", user.getName());
        data.put("password", user.getPassword());
        json.put("data", data);
        return json;
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

    private void sendLogoutSignal() throws IOException {
        JSONObject json = new JSONObject();
        json.put("action", "logout");
        JSONObject data = new JSONObject();

        // this.user = null;
        // System.out.println("You've logged out.");
    }

    // private List<Chatroom> getChatrooms() {
    // this.out.println("getChatrooms");
    // List<Chatroom> chatrooms = (List<Chatroom>)
    // this.objectOutputStream.readObject();
    // return chatrooms;
    // }

    private void handleJoinRoom(Scanner scanner) throws IOException {
        // get list of chatrooms
        JSONObject json = new JSONObject();
        json.put("action", "listRooms");
        JSONObject data = new JSONObject();
        // user name
        data.put("username", this.user.getName());
        json.put("data", data);
        this.out.println(json.toString());
        // read respoonse
        JSONObject responseJson = new JSONObject(this.in.readLine());
        String status = responseJson.getString("status");
        if (status.equals("ok")) {
            System.out.println("Rooms: ");
            JSONArray chatroomsJson = responseJson.getJSONArray("data");
            for (int i = 0; i < chatroomsJson.length(); i++) {
                JSONObject chatroomJson = chatroomsJson.getJSONObject(i);
                System.out.println(i + 1 + ". " + chatroomJson.getString("name"));
            }
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            // if empty
            if (choice.length() == 0) {
                System.out.println("Invalid room number.");
                return;
            }
            // if room number is not valid
            if (Integer.parseInt(choice) < 1 || Integer.parseInt(choice) > chatroomsJson.length()) {
                System.out.println("Invalid room number.");
                return;
            }
            int index = Integer.parseInt(choice) - 1;
            JSONObject chosenChatroomJson = chatroomsJson.getJSONObject(index);
            Chatroom chosenChatroom = new Chatroom(chosenChatroomJson.getString("name"));
            // send joinRoom action
            json = new JSONObject();
            json.put("action", "joinRoom");
            data = new JSONObject();
            // user name
            data.put("username", this.user.getName());
            data.put("roomName", chosenChatroom.getName());
            json.put("data", data);
            this.out.println(json.toString());
            // read respoonse
            responseJson = new JSONObject(this.in.readLine());
            status = responseJson.getString("status");
            if (status.equals("ok")) {
                enterChat(scanner, chosenChatroom.getName());
            } else {
                System.out.println(responseJson.getString("message"));
            }
        } else {
            System.out.println("Error: " + responseJson.getString("message"));
        }
    }

    private void handleCreateRoom(Scanner scanner) throws IOException {
        System.out.println("Enter room name: ");
        System.out.print("> ");
        String roomName = scanner.nextLine().trim();
        Chatroom chatroom = new Chatroom(roomName);
        JSONObject json = new JSONObject();
        json.put("action", "createRoom");
        JSONObject data = new JSONObject();
        // user name
        data.put("username", this.user.getName());
        data.put("roomName", roomName);
        json.put("data", data);
        this.out.println(json.toString());
        // read respoonse
        JSONObject responseJson = new JSONObject(this.in.readLine());
        String status = responseJson.getString("status");
        if (status.equals("ok")) {
            System.out.println(responseJson.getString("message"));
            //
        } else {
            System.out.println(responseJson.getString("message"));
        }
    }

    // room actions
    private void handleRoomsActions(Scanner scanner) throws IOException {
        // 1. Create room
        // 2. Choose room
        // 3. Exit

        boolean exit = false;
        while (!exit) {
            System.out.print("1. Create room\n2. Choose room\n3. Exit\n> ");
            String action = scanner.nextLine().trim();
            switch (action) {
                case "1":
                    handleCreateRoom(scanner);
                    break;
                case "2":
                    handleJoinRoom(scanner);
                    break;
                case "3":
                    this.cleanUp();
                default:
                    break;
            }
        }

    }

    private JSONObject assembleRequest(String action, JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("action", action);
        json.put("data", data);
        return json;
    }

    private void authorizeUser(String action, Scanner scanner) throws IOException, ClassNotFoundException {
        User authUser = promptUserDetails(scanner);
        // send user's action to the server
        // a bit of artificial delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
        JSONObject json = convertUserToJson(authUser, action);
        this.out.println(json.toString());
        JSONObject responseJson = new JSONObject(this.in.readLine());
        String status = responseJson.getString("status");
        // String response = this.in.readLine();
        if (status.equals("ok")) {
            this.user = authUser;

            handleRoomsActions(scanner);
            System.out.println(responseJson.getString("message"));
        } else {
            System.out.println(responseJson.getString("message"));
        }
    }

    private void enterChat(Scanner scanner, String roomName) throws IOException {

        System.out.println(roomName + " Room --- ");

        Thread incomingMessages = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (socket.getInputStream().available() > 0) {
                            JSONObject inComingMsg = new JSONObject(in.readLine());
                            String message = inComingMsg.getString("message");
                            String sender = inComingMsg.getString("sender");
                            System.out.println();
                            System.out.println(sender + ": " + message);
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
                System.out.println("You have left the chat.");
                this.cleanUp();
                break;
            }
            // send action
            JSONObject json = new JSONObject();
            json.put("message", input);
            json.put("roomName", roomName);
            json.put("username", this.user.getName());
            this.out.println(assembleRequest("chat", json).toString());
            // sends name@message to the server
            // this.out.println(user.getName() + "@" + input);
        }
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String action = scanner.nextLine().trim();
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
            sendLogoutSignal();
            if (socket != null)
                socket.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();

            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error cleaning up: " + e.getMessage());
        }
    }

}
