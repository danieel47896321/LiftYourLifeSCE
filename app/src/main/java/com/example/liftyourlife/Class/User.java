package com.example.liftyourlife.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String Uid = "Uid";
    private String Email = "test@email.com";
    private String FullName = "FullName";
    private String FirstName = "FirstName";
    private String lastName = "LastName";
    private String Gender = "Male";
    private String BirthDay = "1/1/1990";
    private String StartDate = "1/1/1990";
    private String Height = "1.7";
    private String Weight = "60";
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
        this.Height = user.getHeight();
        this.Weight = user.getWeight();
    }
    //getters
    public String getEmail() { return Email; }
    public String getFirstName() { return FirstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return Gender; }
    public String getHeight() { return Height; }
    public String getUid() { return Uid; }
    public String getFullName() { return FullName; }
    public String getWeight() { return Weight; }
    public String getBirthDay() {
        return BirthDay;
    }
    public String getStartDate() {
        return StartDate;
    }
    public void setStartDate(String startDate) {
        StartDate = startDate;
    }
    //setters
    public void setBirthDay(String birthDay) {
        BirthDay = birthDay;
    }
    public void setEmail(String email) { Email = email; }
    public void setFirstName(String firstName) { FirstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setGender(String gender) { Gender = gender; }
    public void setUid(String uid) { Uid = uid; }
    public void setHeight(String height) { Height = height; }
    public void setFullName(String fullName) { FullName = fullName; }
    public void setWeight(String weight) { Weight = weight; }
}
