package com.example.liftyourlife.Class;

import java.util.ArrayList;

public class Exercise extends Plan{
    private String Exercise;
    private String Description;
    private String MuscleType;
    private String Sets;
    private String Image;
    public Exercise() {}
    public Exercise(String planName, String date, String day, String uid, String exercise, String description, String muscleType, String image, String reps) {
        super(planName, date, day, uid);
        MuscleType = muscleType;
        Exercise = exercise;
        Description = description;
        Image = image;
        Sets = reps;
    }
    public String getMuscleType() {
        return MuscleType;
    }
    public String getSets() {
        return Sets;
    }
    public void setSets(String sets) {
        Sets = sets;
    }
    public void setMuscleType(String muscleType) {
        MuscleType = muscleType;
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
    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }
}
