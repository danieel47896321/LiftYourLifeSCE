package com.example.liftyourlife.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercise implements Serializable {
    private String Uid;
    private String PlanName;
    private String Date;
    private String Day;
    private String Exercise;
    private String Description;
    private String MuscleType;
    private String Sets;
    private String Image;
    public Exercise() {  }
    public Exercise(String exercise, String description, String muscleType, String image, int reps) {
        MuscleType = muscleType;
        Exercise = exercise;
        Description = description;
        Image = image;
    }
    public String getMuscleType() {
        return MuscleType;
    }
    public void setMuscleType(String muscleType) {
        MuscleType = muscleType;
    }
    public String getSets() {
        return Sets;
    }
    public void setSets(String sets) {
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
    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }
}
