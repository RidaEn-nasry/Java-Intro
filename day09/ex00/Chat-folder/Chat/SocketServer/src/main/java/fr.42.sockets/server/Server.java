
package fr.fortytwo.sockets.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.fortytwo.sockets.server.models.User;
import fr.fortytwo.sockets.server.services.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component("server")
public class Server {

    // public static final int DEFAULT_PORT = 8081;
    // private int port;
    // private static String PORT;
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

    public void initServer() {
        // System.out.println("Server listening...");
        try {
            Socket client = serverSocket.accept();
            PrintWriter out = null;
            if (client.isConnected()) {
                out = new PrintWriter(client.getOutputStream(), true);
                out.println("Hello from server!");
        
            }
            // we will be sent a user object from the client, our user implements the serializable interface

            // and will register the user in the database

            
            // InputStream inputStream = client.getInputStream();
            // BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            // String line = bufferedReader.readLine();
            // System.out.println(line);
            // User user = new User(line, "password");
            // usersService.signup(user.getName(), user.getPassword());

        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            System.exit(1);
        }
    }

}
