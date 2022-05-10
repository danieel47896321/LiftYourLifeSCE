package com.example.liftyourlife.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftyourlife.Class.Exercise;
import com.example.liftyourlife.Class.ExerciseSet;
import com.example.liftyourlife.Class.PopUpMSG;
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

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.MyViewHolder> {
    private Context context;
    private Exercise exercise;
    private List<ExerciseSet> exerciseSets;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    public SetsAdapter(Context context, Exercise exercise, List<ExerciseSet> exerciseSets) {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        this.exerciseSets = new ArrayList<>();
        for(int i=0; i < exerciseSets.size();i++)
            for(int j=0; j<exerciseSets.size();j++)
                if(exerciseSets.get(j).getSetNumber().equals(i+""))
                    this.exerciseSets.add(exerciseSets.get(j));
        this.context = context;
        this.exercise = exercise;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Sets, Reps, Weight;
        ImageView CheckCircle;
        NumberPicker RepsNumberPicker, WeightNumberPicker;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Sets = itemView.findViewById(R.id.Sets);
            Reps = itemView.findViewById(R.id.Reps);
            Weight = itemView.findViewById(R.id.Weight);
            CheckCircle = itemView.findViewById(R.id.CheckCircle);
            RepsNumberPicker = itemView.findViewById(R.id.RepsNumberPicker);
            RepsNumberPicker.setMinValue(0);
            RepsNumberPicker.setMaxValue(300);
            WeightNumberPicker = itemView.findViewById(R.id.WeightNumberPicker);
            WeightNumberPicker.setMinValue(0);
            WeightNumberPicker.setMaxValue(300);
        }
    }
    public SetsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.set_view,parent,false);
        return new SetsAdapter.MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull SetsAdapter.MyViewHolder holder, int position) {
        ExerciseSet exerciseSet = exerciseSets.get(position);
        holder.RepsNumberPicker.setValue(Integer.valueOf(exerciseSet.getReps()));
        holder.WeightNumberPicker.setValue(Integer.valueOf(exerciseSet.getWeight()));
        holder.Sets.setText((Integer.valueOf(exerciseSet.getSetNumber())+1)+"");
        holder.Reps.setText(context.getResources().getString(R.string.Reps) + ":");
        holder.Weight.setText(context.getResources().getString(R.string.Weight) + ":");
        holder.CheckCircle.setImageResource(R.drawable.check_circle);
        if(exerciseSet.isFinish().equals("true"))
            holder.CheckCircle.setImageResource(R.drawable.finish_check_circle);
        else
            holder.CheckCircle.setImageResource(R.drawable.check_circle);
        holder.CheckCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exerciseSet.isFinish().equals("false")) {
                    exerciseSet.setFinish("true");
                    holder.CheckCircle.setImageResource(R.drawable.finish_check_circle);
                }
                else {
                    exerciseSet.setFinish("false");
                    holder.CheckCircle.setImageResource(R.drawable.check_circle);
                }
                updateSet(exerciseSet,holder.RepsNumberPicker.getValue(),holder.WeightNumberPicker.getValue());
            }
        });
    }
    public void updateSet(ExerciseSet exerciseSet, int reps, int weight){
        HashMap<String,String> map = new HashMap<>();
        map.put("Uid",exerciseSet.getUid());
        map.put("PlanName",exerciseSet.getPlanName());
        map.put("Date",exerciseSet.getDate());
        map.put("Day",exerciseSet.getDay());
        map.put("Exercise",exerciseSet.getExercise());
        map.put("SetNumber",exerciseSet.getSetNumber());
        map.put("Weight",exerciseSet.getWeight());
        map.put("Reps",exerciseSet.getReps());
        map.put("Finish",exerciseSet.isFinish());
        Call<Void> call = retrofitInterface.updateSet(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                } else if (response.code() == 400) { }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) { }
        });
    }
    public int getItemCount() { return exerciseSets.size(); }
}
