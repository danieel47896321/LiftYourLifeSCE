package com.example.liftyourlife.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftyourlife.Class.Exercise;
import com.example.liftyourlife.Class.ExerciseSet;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
import com.example.liftyourlife.Server.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenericPlanAdapter extends RecyclerView.Adapter<GenericPlanAdapter.MyViewHolder> {
    private Context context;
    private List<Exercise> exercises;
    private List<Boolean> isClicked;
    private User user;
    private Intent intent;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    public GenericPlanAdapter(Context context, List<Exercise> exercises, User user) {
        this.context = context;
        this.exercises = exercises;
        this.user = user;
        isClicked = new ArrayList<>();
        for(int i=0; i<exercises.size();i++)
            isClicked.add(false);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Exercise, Description, Sets;
        ImageView ExerciseImage, Arrow, addImage;
        ConstraintLayout constraintLayout;
        LinearLayout linearLayout;
        RecyclerView recyclerView;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Exercise = itemView.findViewById(R.id.Exercise);
            Description = itemView.findViewById(R.id.Description);
            Sets = itemView.findViewById(R.id.Sets);
            ExerciseImage = itemView.findViewById(R.id.ExerciseImage);
            Arrow = itemView.findViewById(R.id.Arrow);
            addImage = itemView.findViewById(R.id.addImage);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            view = itemView.findViewById(R.id.view);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
    public GenericPlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.exercise_view,parent,false);
        return new GenericPlanAdapter.MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull GenericPlanAdapter.MyViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.Exercise.setText(exercise.getMuscleType() + ": " +exercise.getExercise());
        holder.Description.setText(exercise.getDescription());
        holder.Sets.setText(context.getResources().getString(R.string.Sets) + ": " + exercise.getSets());
        holder.ExerciseImage.setImageResource(R.drawable.person);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked.set(position,!isClicked.get(position));
                if(isClicked.get(position)) {
                    holder.Arrow.setImageResource(R.drawable.arrow_up);
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.view.setVisibility(View.VISIBLE);
                    retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    retrofitInterface = retrofit.create(RetrofitInterface.class);
                    HashMap<String,String> map = new HashMap<>();
                    map.put("Uid",exercise.getUid());
                    map.put("PlanName",exercise.getPlanName());
                    map.put("Date",exercise.getDate());
                    map.put("Day",exercise.getDay());
                    map.put("Exercise",exercise.getExercise());
                    Call<List<ExerciseSet>> call = retrofitInterface.SetSets(map);
                    call.enqueue(new Callback<List<ExerciseSet>>() {
                        @Override
                        public void onResponse(Call<List<ExerciseSet>> call, Response<List<ExerciseSet>> response) {
                            if (response.code() == 200) {
                                List<ExerciseSet> exerciseSets;
                                exerciseSets = new ArrayList<>();
                                exerciseSets.clear();
                                for(ExerciseSet exerciseSet : response.body())
                                    exerciseSets.add(exerciseSet);
                                SetsAdapter setsAdapter = new SetsAdapter(context, exercise, exerciseSets);
                                holder.recyclerView.setAdapter(setsAdapter);
                            } else if (response.code() == 400) { }
                        }
                        @Override
                        public void onFailure(Call<List<ExerciseSet>> call, Throwable t) { }
                    });
                }
                else{
                    holder.Arrow.setImageResource(R.drawable.arrow_down);
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.view.setVisibility(View.GONE);

                }
            }
        });
    }
    public int getItemCount() { return exercises.size(); }
}
