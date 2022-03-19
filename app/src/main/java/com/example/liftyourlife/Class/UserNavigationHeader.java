package com.example.liftyourlife.Class;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;

public class UserNavigationHeader {
    public UserNavigationHeader(User user, Context context){
        NavigationView UserNavigationView = ((Activity) context).findViewById(R.id.navigationView);
        View UserImage = UserNavigationView.getHeaderView(0).findViewById(R.id.UserImage);
        TextView UserFullName = UserNavigationView.getHeaderView(0).findViewById(R.id.user_fullname);
        TextView UserWeight = UserNavigationView.getHeaderView(0).findViewById(R.id.user_weight);
        TextView UserHeight = UserNavigationView.getHeaderView(0).findViewById(R.id.user_height);
        TextView UserEmail = UserNavigationView.getHeaderView(0).findViewById(R.id.user_email);
        UserFullName.setText(user.getFirstName()+" "+user.getLastName());
        UserHeight.setText(context.getResources().getString(R.string.Height)+": "+user.getHeight() +" "+context.getResources().getString(R.string.Meters));
        UserWeight.setText(context.getResources().getString(R.string.Weight)+": "+user.getWeight() +" "+context.getResources().getString(R.string.Kg));
        UserEmail.setText(user.getEmail());
        if(!user.getImage().equals("Image")){

        }
    }
}
