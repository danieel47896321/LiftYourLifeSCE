package com.example.liftyourlife.Class;

public class ExerciseSet {
    private int SetNumber;
    private int Weight;
    private int Rest;
    public ExerciseSet(int setNumber, int weight, int rest) {
        SetNumber = setNumber;
        Weight = weight;
        Rest = rest;
    }
    public int getSetNumber() {
        return SetNumber;
    }
    public void setSetNumber(int setNumber) {
        SetNumber = setNumber;
    }
    public int getWeight() {
        return Weight;
    }
    public void setWeight(int weight) {
        Weight = weight;
    }
    public int getRest() {
        return Rest;
    }
    public void setRest(int rest) {
        Rest = rest;
    }
}
