package com.example.liftyourlife.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable {
    private String PlanName;
    private String Date;
    private String Day;
    private ArrayList<Exercise> exercises;
    public Plan(){}
    public Plan(String planName, String date, String day) {
        exercises = new ArrayList<>();
        Day = day;
        PlanName = planName;
        Date = date;
    }
    public String getDay() {
        return Day;
    }
    public void setDay(String day) {
        Day = day;
    }
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }
    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
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
