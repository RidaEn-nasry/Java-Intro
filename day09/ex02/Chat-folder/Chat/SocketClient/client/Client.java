
package fr.fortytwo.sockets.client;

public interface Client {

    // action handler to take action based on user input and call the appropriate
    public void start();

    public void stop();

    public void cleanUp();

}