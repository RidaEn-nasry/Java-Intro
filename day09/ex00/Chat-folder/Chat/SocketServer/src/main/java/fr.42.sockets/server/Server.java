
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

@Component("server")
public class Server {
    private UsersService usersService;
    private ServerSocket serverSocket;

    public Server() {

    }

    @Autowired
    public Server(@Qualifier("serverSocket") ServerSocket serverSocket,
            @Qualifier("usersServiceImpl") UsersService usersService) throws IOException, IllegalArgumentException {
        this.serverSocket = serverSocket;
        this.usersService = usersService;
    }

    private Object readObjectFromSocket(Socket client) throws IOException, ClassNotFoundException {
        // if there's an incoming object, we will read it
        InputStream inputStream = client.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Object object = objectInputStream.readObject();
        return object;

    }

    private boolean isDataAvailable(Socket client) throws IOException {
        return client.getInputStream().available() > 0;
    }

    public void initServer() {
        // System.out.println("Server listening...");
        try {
            Socket client = serverSocket.accept();
            PrintWriter out = null;
            if (client.isConnected()) {
                out = new PrintWriter(client.getOutputStream(), true);
                out.println("Hello from server!");
                while (true && !client.isClosed()) {
                    if (isDataAvailable(client)) {
                        Object object = readObjectFromSocket(client);
                        if (object instanceof User) {
                            User user = (User) object;
                            usersService.signup(user.getName(), user.getPassword());
                            out.println("Successful!");
                            break;
                        }
                    }
                }

            }

        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

}
