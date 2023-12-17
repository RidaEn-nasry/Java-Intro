
package fr.fortytwo.sockets.server.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContextExtensionsKt;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.fortytwo.sockets.server.services.UsersService;
import fr.fortytwo.sockets.server.services.UsersServiceImpl;
import fr.fortytwo.sockets.server.config.SocketsApplicationConfig;
import fr.fortytwo.sockets.server.repositories.UsersRepository;
import fr.fortytwo.sockets.server.repositories.UsersRepositoryImpl;
import fr.fortytwo.sockets.server.server.Server;
import fr.fortytwo.sockets.server.services.UsersService;
import fr.fortytwo.sockets.server.models.User;

import java.net.ServerSocket;
import java.util.List;

public class Main {

    private static void testingUserRepository(ApplicationContext context) {
        try {
            System.out.println("Hello from server!");

            UsersRepositoryImpl usersRepositoryImpl = context.getBean(UsersRepositoryImpl.class, "usersRepositoryImpl");
            assert usersRepositoryImpl.getDataSource() != null;

            //
            User user = new User("rida", "1234");
            usersRepositoryImpl.save(user);

            //
            User user2 = usersRepositoryImpl.findById(1L);
            System.out.println("findById: " + user);

            List<User> users = usersRepositoryImpl.findAll();
            System.out.println("FindAll: ");
            for (User eachUser : users) {
                System.out.println(eachUser);
            }

            //
            user2.setName("anotherName");
            usersRepositoryImpl.update(user2);
            user2 = usersRepositoryImpl.findById(user2.getId());
            System.out.println("update: " + user2);

            //
            usersRepositoryImpl.delete(1L);
            List<User> newUsers = usersRepositoryImpl.findAll();
            if (newUsers.size() == 0) {
                System.out.println("update: done ");
            }
        } catch (Exception e) {
            System.err.println("ERR: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        String[] argument = args[0].split("=");
        if (args.length != 1 || !argument[0].equals("--port")) {
            System.err.println("Usage: java Main <port>");
            System.exit(1);
        }
        int port = Server.parsePort(argument[1]);
        ServerSocket serverSocket = (ServerSocket) context.getBean("serverSocket");
        // Server server = context.getBean(Server.class, "server");
        // server.init(serverSocket);
        // assert server.getServerSocket() != null;
        // assert server.getUsersService() != null;
    }
}
