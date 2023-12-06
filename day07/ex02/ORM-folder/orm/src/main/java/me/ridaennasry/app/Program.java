
package me.ridaennasry.orm.app;

import me.ridaennasry.orm.manager.OrmManager;
import java.sql.SQLException;
import me.ridaennasry.orm.models.User;
import java.sql.SQLException;

public class Program {
    static String userName = "orm";
    static String password = "orm";
    static String url = "jdbc:postgresql://localhost:5432/orm_db";

    public static void main(String args[]) {
        Class<?>[] classes = {
                User.class
        };
        OrmManager om = null;
        try {
            om = new OrmManager(url, userName, password);
            om.initSchema(classes);

            // create user
            User user = new User();
            user.setFirstName("Rida");
            user.setLastName("Ennasry");
            user.setAge(25);

            om.save(user);
            System.out.println("User created successfully: \n" + user);
            // update user
            user.setFirstName("Ridaa");
            user.setLastName(null);
            om.update(user);
            System.out.println("User updated successfully: \n" + user);
            // findById
            User user2 = om.findById(user.getId(), User.class);
            System.out.println(user);
            om.closeConnection();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            // stacktrace
            e.printStackTrace();
            if (om != null)
                om.closeConnection();
        }

    }
}
