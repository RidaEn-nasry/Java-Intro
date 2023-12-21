
package fr.fortytwo.sockets.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.fortytwo.sockets.models.User;
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

import javax.swing.text.html.Option;

import java.util.HashMap;

@Component("server")
public class Server {
    private UsersService usersService;
    private ServerSocket serverSocket;
    // set of all clients id and their sockets
    Map<Long, Socket> onlineClients = new HashMap<Long, Socket>();

    public Server() {

    }

    private class UserThread extends Thread {
        private User user;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

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

        private void writeLineToSocket(String msg) {
            if (socket != null && socket.isConnected()) {
                out.println(msg);
            } else {
                System.err.println("Err: Socket hangup.");
                System.exit(1);
            }
        }

        public UserThread(Socket socket) {
            try {
                this.socket = socket;
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("Hello from server!");
            } catch (IOException e) {
                System.err.println("Err: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }

        private Object readObjectFromSocket() throws IOException, ClassNotFoundException {
            // if there's an incoming object, we will read it
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object object = objectInputStream.readObject();
            return object;

        }

        private Optional<User> signInUser() throws IOException, ClassNotFoundException {
            Object object = readObjectFromSocket();
            if (object instanceof User) {
                try {
                    User prospect = (User) object;
                    this.user = usersService.signin(prospect.getName(), prospect.getPassword());
                    return Optional.of(this.user);
                } catch (Exception e) {
                    System.err.println("Err: " + e.getMessage());
                }
            }
            return Optional.empty();
        }

        private Optional<User> signUpUser() throws IOException, ClassNotFoundException {
            Object obj = readObjectFromSocket();
            if (obj instanceof User) {
                try {
                    User prospect = (User) obj;
                    User user = usersService.signup(prospect.getName(), prospect.getPassword());
                    return Optional.of(user);
                } catch (Exception e) {
                    System.err.println("Err: " + e.getMessage());
                }
            }
            return Optional.empty();
        }

        private void auth(String auth) throws IOException, ClassNotFoundException {
            Optional<User> optionalUser;
            String res = null;
            if (auth == "signIn") {
                optionalUser = signInUser();
            } else {
                optionalUser = signUpUser();
            }
            optionalUser.ifPresentOrElse((user) -> {
                this.user = user;
                synchronized (onlineClients) {
                    onlineClients.put(user.getId(), socket);
                }
                writeLineToSocket("Start messaging");
                System.out.println("User is " + user.getName());
            }, () -> {
                writeLineToSocket("Unauthorized");
            });
        }

        public void run() {
            boolean exitThread = false;
            while (!exitThread) {
                try {
                    String action = readLineFromSocket();
                    switch (action) {
                        case "signIn":
                            auth(action);
                            break;
                        case "signUp":
                            auth(action);
                            break;
                        case "Exit":
                            exitThread = true;
                            synchronized (onlineClients) {
                                onlineClients.remove(this.user.getId());
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    System.err.println("Err: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            return;
        }

    }

    @Autowired
    public Server(@Qualifier("serverSocket") ServerSocket serverSocket,
            @Qualifier("usersServiceImpl") UsersService usersService) throws IOException, IllegalArgumentException {
        this.serverSocket = serverSocket;
        this.usersService = usersService;
    }

    private boolean isDataAvailable(Socket client) throws IOException {
        return client.getInputStream().available() > 0;
    }

    public void initServer() {
        // System.out.println("Server listening...");

        // launch a new thread for each client connection

        while (true) {
            try {
                Socket client = serverSocket.accept();
                new UserThread(client).start();
            } catch (Exception e) {
                System.err.println("Err: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }

        }
    }

}
