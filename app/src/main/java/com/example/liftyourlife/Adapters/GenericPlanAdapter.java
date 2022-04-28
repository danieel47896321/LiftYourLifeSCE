package com.example.liftyourlife.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftyourlife.Class.Exercise;
import com.example.liftyourlife.Class.Plan;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
import com.example.liftyourlife.User.GenericPlan;

import java.util.ArrayList;
import java.util.List;

public class GenericPlanAdapter extends RecyclerView.Adapter<GenericPlanAdapter.MyViewHolder> {
    private Context context;
    private List<Exercise> exercises;
    private List<Boolean> isClicked;
    private User user;
    private Intent intent;
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
        holder.Exercise.setText(exercise.getExercise());
        holder.Description.setText(exercise.getDescription());
        holder.Sets.setText(context.getResources().getString(R.string.Sets) + ": " + exercise.getSets().size());
        holder.ExerciseImage.setImageResource(R.drawable.person);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked.set(position,!isClicked.get(position));
                if(isClicked.get(position)) {
                    holder.Arrow.setImageResource(R.drawable.arrow_up);
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.view.setVisibility(View.VISIBLE);
                    SetsAdapter setsAdapter = new SetsAdapter(context, exercises.get(position).getSets());
                    holder.recyclerView.setAdapter(setsAdapter);
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
