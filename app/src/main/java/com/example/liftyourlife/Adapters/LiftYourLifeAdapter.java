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
import com.example.liftyourlife.Guest.About;
import com.example.liftyourlife.Guest.Contact;
import com.example.liftyourlife.Guest.CreateAccount;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.Guest.ResetPassword;
import com.example.liftyourlife.Guest.SignIn;
import com.example.liftyourlife.R;
import com.example.liftyourlife.User.ChangePassword;
import com.example.liftyourlife.User.Home;
import com.example.liftyourlife.User.MyWorkOut;
import com.example.liftyourlife.User.Profile;
import com.example.liftyourlife.User.Statistics;
import com.example.liftyourlife.User.WorkOutUpdate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class LiftYourLifeAdapter extends RecyclerView.Adapter<LiftYourLifeAdapter.MyViewHolder> {
    private Context context;
    private List<Tag> Select;
    private User user;
    private Intent intent;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public LiftYourLifeAdapter(Context context, List<Tag> select, User user) {
        this.context = context;
        this.Select = select;
        this.user = user;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView liftyourlife_text;
        ImageView liftyourlife_image;
        ConstraintLayout liftyourlife_pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            liftyourlife_text = itemView.findViewById(R.id.liftyourlife_text);
            liftyourlife_image = itemView.findViewById(R.id.liftyourlife_image);
            liftyourlife_pic = itemView.findViewById(R.id.liftyourlife_pic);
        }
    }
    public LiftYourLifeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.liftyourlife_view,parent,false);
        return new LiftYourLifeAdapter.MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull LiftYourLifeAdapter.MyViewHolder holder, int position) {
        holder.liftyourlife_text.setText(Select.get(position).getTagName());
        holder.liftyourlife_image.setImageResource(Select.get(position).getPhoto());
        holder.liftyourlife_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, Home.class);
                if(holder.liftyourlife_text.getText().equals(context.getResources().getString(R.string.Profile)))
                    intent = new Intent(context, Profile.class);
                else if(holder.liftyourlife_text.getText().equals(context.getResources().getString(R.string.ChangePassword)))
                    intent = new Intent(context, ChangePassword.class);
                else if(holder.liftyourlife_text.getText().equals(context.getResources().getString(R.string.MyWorkOut)))
                    intent = new Intent(context, MyWorkOut.class);
                else if(holder.liftyourlife_text.getText().equals(context.getResources().getString(R.string.WorkOutUpdate)))
                    intent = new Intent(context, WorkOutUpdate.class);
                else if(holder.liftyourlife_text.getText().equals(context.getResources().getString(R.string.Statistics)))
                    intent = new Intent(context, Statistics.class);
                else
                    intent = new Intent(context, Home.class);
                if(holder.liftyourlife_text.getText().equals(context.getResources().getString(R.string.Logout))) {
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