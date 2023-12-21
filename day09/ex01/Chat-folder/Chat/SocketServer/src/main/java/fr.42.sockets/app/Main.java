
package fr.fortytwo.sockets.server.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContextExtensionsKt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.simple.SimpleLogger;
import fr.fortytwo.sockets.server.services.UsersService;
import fr.fortytwo.sockets.server.services.UsersServiceImpl;
import fr.fortytwo.sockets.server.config.SocketsApplicationConfig;
import fr.fortytwo.sockets.server.repositories.UsersRepository;
import fr.fortytwo.sockets.server.repositories.UsersRepositoryImpl;
import fr.fortytwo.sockets.server.server.Server;
import fr.fortytwo.sockets.server.services.UsersService;
import fr.fortytwo.sockets.models.User;

import java.net.ServerSocket;
import java.util.List;

public class Main {

 
    public static void main(String[] args) {
        // set log level
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "Error");
        String[] argument = args[0].split("=");
        if (args.length != 1 || !argument[0].equals("--port") || argument[1].length() == 0) {
            System.err.println("Usage: java Main <port>");
            System.exit(1);
        }
        System.setProperty("server.port", argument[1]);
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
            Server server = context.getBean(Server.class, "serverSocket");
            server.initServer();
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }
}
