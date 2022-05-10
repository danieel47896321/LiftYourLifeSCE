package com.example.liftyourlife.Server;
import com.example.liftyourlife.Class.Exercise;
import com.example.liftyourlife.Class.ExerciseSet;
import com.example.liftyourlife.Class.Plan;
import com.example.liftyourlife.Class.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface RetrofitInterface {
    @POST("/login")
    Call<User> getUser(@Body HashMap<String, String> map);
    @POST("/signup")
    Call<Void> sendUser(@Body HashMap<String, String> map);
    @POST("/profile")
    Call<Void> updateProfile(@Body HashMap<String, String> map);
    @POST("/setPlans")
    Call<List<Plan>> setPlans(@Body HashMap<String, String> map);
    @POST("/addPlan")
    Call<Void> AddPlan(@Body HashMap<String, String> map);
    @POST("/removePlan")
    Call<Void> RemovePlan(@Body HashMap<String, String> map);
    @POST("/setExercises")
    Call<List<Exercise>> setExercises(@Body HashMap<String, String> map);
    @POST("/addExercise")
    Call<Void> AddExercise(@Body HashMap<String, String> map);
    @POST("/removeExercise")
    Call<Void> RemoveExercise(@Body HashMap<String, String> map);
    @POST("/updateSet")
    Call<Void> updateSet(@Body HashMap<String, String> map);
    @POST("/addSet")
    Call<Void> AddSet(@Body HashMap<String, String> map);
    @POST("/setSets")
    Call<List<ExerciseSet>> SetSets(@Body HashMap<String, String> map);
}
