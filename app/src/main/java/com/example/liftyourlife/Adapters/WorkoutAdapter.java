package com.example.liftyourlife.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftyourlife.Class.Plan;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
import com.example.liftyourlife.User.GenericPlan;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.MyViewHolder> {
    private Context context;
    private List<Plan> planList;
    private User user;
    private Intent intent;
    public WorkoutAdapter(Context context, List<Plan> planList, User user) {
        this.context = context;
        this.planList = planList;
        this.user = user;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView PlanName;
        TextView Date;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PlanName = itemView.findViewById(R.id.PlanName);
            Date = itemView.findViewById(R.id.Date);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
    public WorkoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.plan_view,parent,false);
        return new WorkoutAdapter.MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull WorkoutAdapter.MyViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.PlanName.setText(plan.getPlanName());
        holder.Date.setText(plan.getDate());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.PickColor));
                intent = new Intent(context, GenericPlan.class);
                intent.putExtra("user", user);
                intent.putExtra("plan", plan);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }
    public int getItemCount() { return planList.size(); }
}