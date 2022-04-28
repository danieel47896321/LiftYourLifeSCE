package com.example.liftyourlife.Class;

import java.util.ArrayList;

public class Exercise {
    private String Exercise;
    private String Description;
    private ArrayList<ExerciseSet> Sets;
    private int Image;
    public Exercise(String exercise, String description, int image, int reps) {
        Sets = new ArrayList<>();
        for(int i=0;i<reps;i++)
            Sets.add(new ExerciseSet(i,0,0));
        Exercise = exercise;
        Description = description;
        Image = image;
    }
    public ArrayList<ExerciseSet> getSets() {
        return Sets;
    }
    public void setSets(ArrayList<ExerciseSet> sets) {
        Sets = sets;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
    public String getExercise() {
        return Exercise;
    }
    public void setExercise(String exercise) {
        Exercise = exercise;
    }
    public int getImage() {
        return Image;
    }
    public void setImage(int image) {
        Image = image;
    }
}
