package com.example.liftyourlife.Class;
import java.io.Serializable;
public class Plan implements Serializable {
    private String Uid;
    private String PlanName;
    private String Date;
    private String Day;
    public Plan(){ }
    public Plan(String planName, String date, String day, String uid) {
        Day = day;
        PlanName = planName;
        Date = date;
        Uid = uid;
    }
    public String getUid() {
        return Uid;
    }
    public void setUid(String uid) {
        Uid = uid;
    }
    public String getDay() {
        return Day;
    }
    public void setDay(String day) {
        Day = day;
    }
    public String getPlanName() {
        return PlanName;
    }
    public void setPlanName(String planName) {
        PlanName = planName;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
}
