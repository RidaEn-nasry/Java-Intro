
class Program {

    public static void main(String[] args) {
        UsersArrayList usersArr = new UsersArrayList();
        User[] users = new User[20];
        for (int i = 0; i < 20; i++) {
            users[i] = new User("user" + (i + 1), i * 5);
            usersArr.addUser(users[i]);
        }
        System.out.println("Retreiving " + usersArr.getUsersNum() + " users by ids");
        for (int i = 0; i < 20; i++) {
            System.out.print(usersArr.getUserById(i + 1).getName() + " ");
        }

        // getting a non-existing user by id
        System.out.println();
        try {
            usersArr.getUserById(100);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
    }
}