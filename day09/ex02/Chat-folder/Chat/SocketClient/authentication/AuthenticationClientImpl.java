
package fr.fortytwo.sockets.client.authentication;


import fr.fortytwo.sockets.client.authentication.AuthenticationClient;
import fr.fortytwo.sockets.models.User;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.json.JSONObject;
import org.springframework.stereotype.Component;



/*
 * standard input json
 * {
 * "action": "action_name",
 * "data": {
    * specific data
    * }
 * 
 * 
 * }
 * 
 */

        /*
 * standard response from server:
 * status: ok | notOk
 * message: string
 * data: object | null
 * 
 */


@Component("authenticationClient")
public class AuthenticationClientImpl implements AuthenticationClient {


    private Socket socket;
    // readers and writers
    private BufferedReader reader;
    private PrintWriter writer;

    public AuthenticationClientImpl() {  

    }

    public AuthenticationClientImpl(Socket socket, BufferedReader reader, PrintWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }


    @Override
    public User promptUserDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username: ");
        System.out.print("> ");
        String name = scanner.nextLine().trim();
        System.out.println("Enter password: ");
        System.out.print("> ");
        String password = scanner.nextLine().trim();
        scanner.close();
        return new User(name, password);
    } 


    private String convertUserToJson(User user, String action) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", action);
        jsonObject.put("data", new JSONObject().put("name", user.getName()).put("password", user.getPassword()));
        return jsonObject.toString();

    }
    private JSONObject convertJsonToUser(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        return new User(name, password);
    }


    @Override
    public User authenticate(String username, String password) {
        String req = convertUserToJson(promptUserDetails()); 
        writer.println(req);
        String res = reader.readLine();
        JSONObject jsonObject = new JSONObject(res);
        String status = jsonObject.getString("status");
        User CreatedUser = null;
        if (status.equals("notOk")) {
            // handle error
            // String message = jsonObject.getString("message");
        } else {
            // handle success
            // get user object
            CreatedUser = convertJsonToUser(jsonObject.getJSONObject("data"));
        }
        return CreatedUser;        
    }


    @Override
    public User register(String username, String password) {
        // we need to get user details
        // we need to send user details to server
        // we need to get response from server
        return null; 

    }

  
    @Override
    public boolean logout(String username) {
        return false;
    }

    @Override
    public boolean isLoggedIn(String username) {
        return false;
    }
    
}
