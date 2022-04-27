package com.example.liftyourlife.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftyourlife.Class.Tag;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.R;
import com.example.liftyourlife.User.ChangePassword;
import com.example.liftyourlife.User.Home;
import com.example.liftyourlife.User.WorkOut;
import com.example.liftyourlife.User.Profile;
import com.example.liftyourlife.User.Statistics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context context;
    private List<Tag> Select;
    private Intent intent;
    private User user;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public HomeAdapter(Context context, List<Tag> select, User user) {
        this.context = context;
        this.Select = select;
        this.user = user;
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
    public HomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.tag_view,parent,false);
        return new HomeAdapter.MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull HomeAdapter.MyViewHolder holder, int position) {
        holder.TagName.setText(Select.get(position).getTagName());
        holder.TagImage.setImageResource(Select.get(position).getPhoto());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, Home.class);
                if(holder.TagName.getText().equals(context.getResources().getString(R.string.Profile)))
                    intent = new Intent(context, Profile.class);
                else if(holder.TagName.getText().equals(context.getResources().getString(R.string.ChangePassword)))
                    intent = new Intent(context, ChangePassword.class);
                else if(holder.TagName.getText().equals(context.getResources().getString(R.string.WorkOut)))
                    intent = new Intent(context, WorkOut.class);
                else if(holder.TagName.getText().equals(context.getResources().getString(R.string.Statistics)))
                    intent = new Intent(context, Statistics.class);
                else
                    intent = new Intent(context, Home.class);
                if(holder.TagName.getText().equals(context.getResources().getString(R.string.Logout))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getResources().getString(R.string.Logout)).setMessage(context.getResources().getString(R.string.AreYouSure)).setCancelable(true).setPositiveButton(context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(firebaseAuth.getCurrentUser() != null)
                                firebaseAuth.signOut();
                            GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build();
                            GoogleSignInClient googleClient = GoogleSignIn.getClient(context, options);
                            googleClient.signOut();
                            intent = new Intent(context, LiftYourLife.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    }).setNegativeButton(context.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    }).show();
                }
                else{
                    intent.putExtra("user", user);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });
    }
    public int getItemCount() { return Select.size(); }
}