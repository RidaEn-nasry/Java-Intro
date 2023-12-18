
package fr.fortytwo.sockets.client.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.IllegalArgumentException;
import java.net.Socket;
import java.util.Scanner;

import fr.fortytwo.sockets.client.authentication.AuthenticationClient;

public class Main {

    private static int parsePort(String str) throws IllegalArgumentException {
        int port = Integer.parseInt(str);
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Valid range values for port are 0-65535");
        }
        return port;
    }

    // a function to get a line from server
    // private static String getLineFromServer(BufferedReader in) throws Exception {
    // String line = in.readLine();
    // if (line == null) {
    // throw new Exception("Server disconnected");
    // }
    // return line;
    // }

    public static void main(String[] args) {
        String[] argument = args[0].split("=");
        if (args.length != 1 || !argument[0].equals("--server-port") || argument[1].length() == 0) {
            System.err.println("Usage: java Main <port>");
            System.exit(1);
        }
        try {
            int port = parsePort(argument[1]);
            System.setProperty("server.port", argument[1]);
            Socket socket = new Socket("127.0.0.1", port);
            AuthenticationClient authentication = new AuthenticationClient(socket);
            authentication.start();
            // BufferedReader in = null;
            // if (socket.isConnected()) {
            // in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // String line = getLineFromServer(in);
            // System.out.println(line);
            // }
            // Scanner scanner = new Scanner(System.in);
            // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // String line = null;
            // switch (scanner.next()) {
            // case: "signup":

            // Scanner scanner = new Scanner(System.in);
            // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // String line;
            // while (scanner.hasNext()) {
            // line = scanner.next();
            // System.out.println("a new line entered: " + line);
            // out.println(line);
            // }
            // BufferedReader in = new BufferedReader(new
            // InputStreamReader(socket.getInputStream()));
            // PrintWriter out = new PrintWriter(socket.getOutputStream());
            // while ((line = in.readLine()) != null) {
            // System.out.println(line);
            // }
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
        }

    }

}