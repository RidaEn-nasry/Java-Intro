
package me.ridaennasry.orm.models;

import me.ridaennasry.orm.annotations.OrmColumn;
import me.ridaennasry.orm.annotations.OrmColumnId;
import me.ridaennasry.orm.annotations.OrmEntity;



@OrmEntity(table = "simple_user")
public class User {


    @OrmColumnId
    private Long id;

    @OrmColumn(name = "first_name", length = 10)
    private String firstName;

    @OrmColumn(name = "last_name", length = 10)
    private String lastName;

    @OrmColumn(name = "age")
    private Integer age;

    public User() {
    }

    public User(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User user = (User) obj;
        return user.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    

    // toString
    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName="
                + lastName + ", age=" + age + "]";
    }

}