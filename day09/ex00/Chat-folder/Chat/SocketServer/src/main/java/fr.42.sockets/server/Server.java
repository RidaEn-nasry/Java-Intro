
package fr.fortytwo.sockets.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.fortytwo.sockets.server.services.UsersService;
import java.io.IOException;
import java.net.ServerSocket;

@Component("server")
public class Server {

    // default port DEFAULT_PORT
    // public static final int DEFAULT_PORT = 8081;
    // private int port;
    // private static String PORT;
    private UsersService usersService;
    private ServerSocket serverSocket;

    public static int parsePort(String str) throws IllegalArgumentException {
        int port = Integer.parseInt(str);
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("");
        }
        return port;
    }

    public Server() {

    }

    @Autowired
    public Server(@Qualifier("usersServiceImpl") UsersService usersService)
            throws IOException, IllegalArgumentException {
        // this.serverSocket = serverSocket;
        this.usersService = usersService;
    }

    public void init(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public UsersService getUsersService() {
        return this.usersService;
    }
}
