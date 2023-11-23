
package fr._42.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.Array;
import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;
import fr._42.chat.repositories.UserRepository;
import fr._42.chat.models.User;
import fr._42.chat.models.Chatroom;
import java.sql.Struct;
import org.postgresql.util.PGobject;

public class UserRepositoryJdbcImpl implements UserRepository {
    private DataSource ds;

    public UserRepositoryJdbcImpl(DataSource ds) {
        this.ds = ds;
    }

    private List<Chatroom> convertArrayToChatroomList(Array sqlArray) throws SQLException {
        List<Chatroom> chatroomList = new ArrayList<>();

        if (sqlArray != null) {
            Object[] chatroomArray = (Object[]) sqlArray.getArray();
            for (Object chatroomObj : chatroomArray) {
                if (chatroomObj instanceof PGobject) {
                    // Cast to PGobject - specific for PostgreSQL
                    PGobject pgObject = (PGobject) chatroomObj;
                    // Need to parse the value of PGobject
                    // The actual format depends on how PostgreSQL is returning the composite type
                    // Here, let's assume it's a comma-separated string like "(123,John)"
                    String value = pgObject.getValue();
                    String[] parts = value.replace("(", "").replace(")", "").split(",");

                    Chatroom chatroom = new Chatroom(Long.parseLong(parts[0]), parts[1], null, null);

                    chatroomList.add(chatroom);
                } else {
                    throw new SQLException(
                            "Received an object of unexpected type: " + chatroomObj.getClass().getName());
                }
            }
        }
        return chatroomList;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getLong("user_id"));
        user.setUserLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));

        // Assuming the SQL query returns created_chatrooms and joined_chatrooms
        // as columns with type Array (of structured chatroom data)
        Array createdChatroomsArray = resultSet.getArray("created_chatrooms");
        Array joinedChatroomsArray = resultSet.getArray("joined_chatrooms");

        // Convert Arrays to List<Chatroom>
        user.setCreatedRooms(convertArrayToChatroomList(createdChatroomsArray));
        user.setJoinedRooms(convertArrayToChatroomList(joinedChatroomsArray));

        return user;
    }

    @Override
    public List<User> findAll(int page, int size) {
        /// do stuff here
        List<User> users = new ArrayList<>();
        try (Connection connection = ds.getConnection()) {
            int offset = page * size;
            String sql = "WITH paginated_users AS (" +
                    "SELECT user_id, login, password FROM users ORDER BY user_id LIMIT ? OFFSET ?)," +
                    "created_rooms AS (" +
                    "SELECT chatrooms.*, chatrooms.chatroom_owner AS cr_owner FROM chatrooms " +
                    "WHERE chatrooms.chatroom_owner IN (SELECT user_id FROM paginated_users))," +
                    "joined_rooms AS (" +
                    "SELECT chatrooms.*, user_chatroom.user_id FROM user_chatroom " +
                    "JOIN chatrooms ON user_chatroom.chatroom_id = chatrooms.chatroom_id " +
                    "WHERE user_chatroom.user_id IN (SELECT user_id FROM paginated_users)) " +
                    "SELECT pu.user_id, pu.login, pu.password, " +
                    "array_remove(array_agg(DISTINCT cr.*) FILTER (WHERE cr.chatroom_id IS NOT NULL), NULL) AS created_chatrooms, "
                    +
                    "array_remove(array_agg(DISTINCT jr.*) FILTER (WHERE jr.chatroom_id IS NOT NULL), NULL) AS joined_chatrooms "
                    +
                    "FROM paginated_users pu " +
                    "LEFT JOIN created_rooms cr ON pu.user_id = cr.cr_owner " +
                    "LEFT JOIN joined_rooms jr ON pu.user_id = jr.user_id " +
                    "GROUP BY pu.user_id, pu.login, pu.password;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
}
