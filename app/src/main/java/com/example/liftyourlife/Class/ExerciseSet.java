package com.example.liftyourlife.Class;

import java.io.Serializable;

public class ExerciseSet extends Exercise implements Serializable {
    private String SetNumber;
    private String Weight;
    private String Reps;
    private String Finish = "false";
    public ExerciseSet() { }
    public ExerciseSet(String setNumber, String weight, String reps) {
        SetNumber = setNumber;
        Weight = weight;
        Reps = reps;
    }
    public String isFinish() {
        return Finish;
    }
    public void setFinish(String finish) {
        Finish = finish;
    }
    public String getReps() {
        return Reps;
    }
    public void setReps(String reps) {
        Reps = reps;
    }
    public String getSetNumber() {
        return SetNumber;
    }
    public void setSetNumber(String setNumber) {
        SetNumber = setNumber;
    }
    public String getWeight() {
        return Weight;
    }
    public void setWeight(String weight) {
        Weight = weight;
    }
}
