import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Program {
    public static void main(String[] args) {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream("DataFile");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            User user = new User(21, "Name");
            System.out.println("Name: " + user.getName() + " age " + user.getAge());

            objectOutputStream.writeObject(user);
            FileInputStream fileInputStream = new FileInputStream("DataFile");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            User user2 = (User) objectInputStream.readObject();
            System.out.println("Name: " + user2.getName() + " age " + user2.getAge());

            objectInputStream.close();
            objectOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}


class User implements Serializable {
    private int age;
    private String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setString(String name) {
        this.name = name;
    }

}
