
class UsersArrayList implements UserList {

    private User[] users = new User[10];
    private int len = 0;

    public void addUser(User user) {
        if (len == users.length) {
            User[] newUsers = new User[(int) (users.length * 1.5)];
            int i = 0;
            for (; i < users.length; i++) {
                newUsers[i] = users[i];
            }
            users = newUsers;
        }
        users[len] = user;
        len += 1;
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user == null) {
                break;
            }
            else if (user.getId() == id) {
                return user;
            }
        }
        throw new UserNotFoundException("user with id: " + id + " doesn't exist");
    }

    public User getUserByIndex(int index) {
        if (index >= len - 1 || index < 0) {
            return null;
            // throw new UserNotFoundException("user with index: " + index + " doesn't exist");
        } else {
            
            return users[index];
        }
    }

    public int getUsersNum() {
        return len;
    }

}