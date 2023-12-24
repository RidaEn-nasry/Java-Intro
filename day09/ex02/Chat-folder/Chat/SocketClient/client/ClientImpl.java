
package fr.fortytwo.sockets.client;

import org.springframework.stereotype.Component;

import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import fr.fortytwo.sockets.client.authentication.AuthenticationClient;

@Component("client")
public class ClientImpl implements Client {

    private Socket socket;

    @Autowired
    private AuthenticationClient authenticationClient;

    public ClientImpl() {
    }

    public ClientImpl(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void start() {
        System.out.println("Client started");
    }

    @Override
    public void stop() {
        System.out.println("Client stopped");
    }

    @Override
    public void cleanUp() {
        System.out.println("Client cleaned up");
    }

}