package com.example.liftyourlife.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liftyourlife.Class.Tag;
import com.example.liftyourlife.Guest.About;
import com.example.liftyourlife.Guest.Contact;
import com.example.liftyourlife.Guest.CreateAccount;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.Guest.ResetPassword;
import com.example.liftyourlife.Guest.SignIn;
import com.example.liftyourlife.R;
import java.util.List;

public class LiftYourLifeAdapter extends RecyclerView.Adapter<LiftYourLifeAdapter.MyViewHolder> {
    private Context context;
    private List<Tag> Select;
    private Intent intent;
    public LiftYourLifeAdapter(Context context, List<Tag> select) {
        this.context = context;
        this.Select = select;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView TagName;
        ImageView TagImage;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TagName = itemView.findViewById(R.id.TagName);
            TagImage = itemView.findViewById(R.id.TagImage);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
    public LiftYourLifeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.tag_view,parent,false);
        return new LiftYourLifeAdapter.MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull LiftYourLifeAdapter.MyViewHolder holder, int position) {
        holder.TagName.setText(Select.get(position).getTagName());
        holder.TagImage.setImageResource(Select.get(position).getPhoto());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
                if(holder.TagName.getText().equals(context.getResources().getString(R.string.ResetPassword)))
                    intent = new Intent(context, ResetPassword.class);
                else if(holder.TagName.getText().equals(context.getResources().getString(R.string.SignIn)))
                    intent = new Intent(context, SignIn.class);
                else if(holder.TagName.getText().equals(context.getResources().getString(R.string.CreateAccount)))
                    intent = new Intent(context, CreateAccount.class);
                else if(holder.TagName.getText().equals(context.getResources().getString(R.string.Contact)))
                    intent = new Intent(context, Contact.class);
                else if(holder.TagName.getText().equals(context.getResources().getString(R.string.About)))
                    intent = new Intent(context, About.class);
                else
                    intent = new Intent(context, LiftYourLife.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }
    public int getItemCount() { return Select.size(); }
}