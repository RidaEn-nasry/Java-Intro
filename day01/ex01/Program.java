
class Program {
    public static void main(String[] args) {
        User user1 = new User("user1", 100);
        User user2 = new User("user2", 200);
        User user3 = new User("user3", 10);

        System.out.println("-> " + user1.getName() + " with id: " + user1.getId() + " was created!");

        System.out.println("-> " + user2.getName() + " with id: " + user2.getId() + " was created!");

        System.out.println("-> " + user3.getName() + " with id: " + user3.getId() + " was created!");

    }

}