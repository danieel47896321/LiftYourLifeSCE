package com.example.liftyourlife.Class;

import java.io.Serializable;

public class User implements Serializable {
    private String Uid = "Uid";
    private String Email = "test@email.com";
    private String FullName = "FullName";
    private String FirstName = "FirstName";
    private String lastName = "LastName";
    private String Gender = "Male";
    private String Age = "18";
    private String Image = "Image";
    private String City = "באר שבע";
    public User(String firstName, String lastName, String email) {
        FirstName = firstName;
        this.lastName = lastName;
        Email = email;
    }
    public User() {}
    public User(User user) {
        this.Uid = user.getUid();
        this.Email = user.getEmail();
        this.FirstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.Gender = user.getGender();
        this.Image = user.getImage();
        this.City = user.getCity();
    }
    //getters
    public String getEmail() { return Email; }
    public String getFirstName() { return FirstName; }
    public String getLastName() { return lastName; }
    public String getImage() { return Image; }
    public String getGender() { return Gender; }
    public String getAge() { return Age; }
    public String getCity() { return City; }
    public String getUid() { return Uid; }
    public String getFullName() { return FullName; }
    //setters
    public void setEmail(String email) { Email = email; }
    public void setFirstName(String firstName) { FirstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setImage(String image) { Image = image; }
    public void setGender(String gender) { Gender = gender; }
    public void setAge(String age) { Age = age; }
    public void setUid(String uid) { Uid = uid; }
    public void setCity(String city) { City = city; }
    public void setFullName(String fullName) { FullName = fullName; }
}
