interface UserList {

    abstract public void addUser(User user);

    abstract public User getUserById(int id);

    abstract public User getUserByIndex(int index);

    abstract public int getUsersNum();
}
