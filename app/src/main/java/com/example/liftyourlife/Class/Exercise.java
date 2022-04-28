package com.example.liftyourlife.Class;

public class Exercise {
    private String Exercise;
    private int Weight;
    private int Sets;
    private int Reps;
    private int Rest;
    private int Image;
    public Exercise(String exercise, int sets, int reps, int rest, int image, int weight) {
        Exercise = exercise;
        Sets = sets;
        Reps = reps;
        Rest = rest;
        Image = image;
        Weight = weight;
    }
    public int getWeight() {
        return Weight;
    }
    public void setWeight(int weight) {
        Weight = weight;
    }
    public String getExercise() {
        return Exercise;
    }
    public void setExercise(String exercise) {
        Exercise = exercise;
    }
    public int getSets() {
        return Sets;
    }
    public void setSets(int sets) {
        Sets = sets;
    }
    public int getReps() {
        return Reps;
    }
    public void setReps(int reps) {
        Reps = reps;
    }
    public int getRest() {
        return Rest;
    }
    public void setRest(int rest) {
        Rest = rest;
    }
    public int getImage() {
        return Image;
    }
    public void setImage(int image) {
        Image = image;
    }
}
