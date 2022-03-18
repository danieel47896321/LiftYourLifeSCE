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

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.MyViewHolder> {
    private Context context;
    private List<Tag> Select;
    private Intent intent;
    public TagsAdapter(Context context, List<Tag> select) {
        this.context = context;
        this.Select = select;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        ConstraintLayout cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tag_name);
            imageView = itemView.findViewById(R.id.tag_photo);
            cardView = itemView.findViewById(R.id.engineeringview_id);
        }
    }
    public TagsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.tag_view,parent,false);
        return new TagsAdapter.MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull TagsAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(Select.get(position).getTagName());
        holder.imageView.setImageResource(Select.get(position).getPhoto());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.gray));
                if(holder.textView.getText().equals(context.getResources().getString(R.string.ResetPassword)))
                    intent = new Intent(context, ResetPassword.class);
                else if(holder.textView.getText().equals(context.getResources().getString(R.string.SignIn)))
                    intent = new Intent(context, SignIn.class);
                else if(holder.textView.getText().equals(context.getResources().getString(R.string.CreateAccount)))
                    intent = new Intent(context, CreateAccount.class);
                else if(holder.textView.getText().equals(context.getResources().getString(R.string.Contact)))
                    intent = new Intent(context, Contact.class);
                else if(holder.textView.getText().equals(context.getResources().getString(R.string.About)))
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